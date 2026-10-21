package com.example.smartnews.service.impl;

import com.example.smartnews.dto.request.ExternalArticleSearchRequest;
import com.example.smartnews.dto.request.ImportExternalArticleRequest;
import com.example.smartnews.dto.response.ExternalArticleDetailResponse;
import com.example.smartnews.dto.response.ExternalArticleResponse;
import com.example.smartnews.entity.ExternalArticle;
import com.example.smartnews.entity.ExternalSource;
import com.example.smartnews.exception.ResourceNotFoundException;
import com.example.smartnews.repository.ExternalArticleRepository;
import com.example.smartnews.repository.ExternalSourceRepository;
import com.example.smartnews.service.ExternalArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.smartnews.dto.request.ImportExternalArticleRequest;
import com.example.smartnews.entity.ExternalSource;
import com.example.smartnews.exception.BadRequestException;
import com.example.smartnews.repository.ExternalSourceRepository;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import static com.example.smartnews.specification.ExternalArticleSpecification.*;

@Service
@RequiredArgsConstructor
public class ExternalArticleServiceImpl
        implements ExternalArticleService {

    private final ExternalSourceRepository sourceRepo;

    private final ExternalArticleRepository articleRepo;


    @Override
    @Transactional(readOnly = true)
    public Page<ExternalArticleResponse> search(
            ExternalArticleSearchRequest request
    ) {

        Specification<ExternalArticle> spec =
                Specification
                        .where(hasKeyword(request.getKeyword()))
                        .and(hasSource(request.getSourceId()))
                        .and(hasCategory(request.getCategoryId()))
                        .and(hasOriginalCategory(request.getOriginalCategory()))
                        .and(publishedAfter(request.getFromDate()))
                        .and(publishedBefore(request.getToDate()));

        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                buildSort(request.getSortBy())
        );
        return articleRepo
                .findAll(spec, pageable)
                .map(this::mapToResponse);
    }


    @Override
    @Transactional(readOnly = true)
    public ExternalArticleDetailResponse getById(
            Integer id
    ) {
        ExternalArticle article = articleRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("External article not found"));

        return mapToDetailResponse(article);
    }


    private Sort buildSort(String sortBy) {

        if (sortBy == null || sortBy.isBlank()) {
            return Sort.by("publishedAt").descending();
        }

        return switch (sortBy) {
            case "oldest" -> Sort.by("publishedAt").ascending();
            case "title" -> Sort.by("title").ascending();
            case "recentlyImported" -> Sort.by("importedAt").descending();
            default -> Sort.by("publishedAt").descending();
        };
    }


    private ExternalArticleResponse mapToResponse(
            ExternalArticle article
    ) {
        Integer categoryId = null;
        String categoryName = null;
        if (article.getCategory() != null) {
            categoryId = article.getCategory().getCategoryId();
            categoryName = article.getCategory().getCategoryName();
        }

        return ExternalArticleResponse.builder()
                .externalArticleId(article.getExternalArticleId())
                .title(article.getTitle())
                .url(article.getUrl())
                .imageUrl(article.getImageUrl())
                .author(article.getAuthor())
                .publishedAt(article.getPublishedAt())
                .originalCategory(article.getOriginalCategory())
                .categoryId(categoryId)
                .categoryName(categoryName)
                .sourceId(article.getSource().getSourceId())
                .sourceCode(article.getSource().getCode())
                .sourceName(article.getSource().getName())
                .build();
    }


    private ExternalArticleDetailResponse mapToDetailResponse(
            ExternalArticle article
    ) {

        Integer categoryId = null;
        String categoryName = null;

        if (article.getCategory() != null) {
            categoryId = article.getCategory().getCategoryId();
            categoryName = article.getCategory().getCategoryName();
        }

        return ExternalArticleDetailResponse.builder()
                .externalArticleId(article.getExternalArticleId())
                .sourceArticleKey(article.getSourceArticleKey())
                .title(article.getTitle())
                .summary(article.getSummary())
                .content(article.getContent())
                .url(article.getUrl())
                .imageUrl(article.getImageUrl())
                .author(article.getAuthor())
                .publishedAt(article.getPublishedAt())
                .importedAt(article.getImportedAt())
                .originalCategory(article.getOriginalCategory())
                .categoryId(categoryId)
                .categoryName(categoryName)
                .sourceId(article.getSource().getSourceId())
                .sourceCode(article.getSource().getCode())
                .sourceName(article.getSource().getName())
                .build();
    }

    @Override
    @Transactional
    public ExternalArticleDetailResponse importFromUrl(
            ImportExternalArticleRequest request
    ) {

        String inputUrl = request.getUrl().trim();
        URI uri = validateUrl(inputUrl);
        ExternalSource source = findSourceByHost(uri.getHost());
        Document document = fetchDocument(inputUrl);
        String canonicalUrl = getCanonicalUrl(document, inputUrl);

        if (articleRepo.existsByUrl(canonicalUrl)) {
            throw new BadRequestException("Article already imported");
        }

        ExternalArticle article = new ExternalArticle();

        article.setSource(source);
        article.setTitle(extractTitle(document));
        article.setSummary(
                extractMeta(document, "meta[property=og:description]", "content"));
        article.setImageUrl(
                extractMeta(document, "meta[property=og:image]", "content"));
        article.setAuthor(extractAuthor(document));
        article.setOriginalCategory(extractCategory(document));
        article.setPublishedAt(extractPublishedAt(document));
        article.setUrl(canonicalUrl);
        article.setContent(null);
        article.setImportedAt(LocalDateTime.now());

        ExternalArticle saved = articleRepo.save(article);

        return mapToDetailResponse(saved);
    }

    private URI validateUrl(String url) {
        try {
            URI uri = new URI(url);
            String schema = uri.getScheme();
            String host = uri.getHost();
            if(schema == null || host == null){
                throw new BadRequestException("Invalid article URL");
            }
            if(!schema.equalsIgnoreCase("https")
                    && !schema.equalsIgnoreCase("http")){
                throw new BadRequestException("Only HTTP and HTTPS URLs are supported");
            }
            return uri;
        }catch (URISyntaxException ex){
            throw new BadRequestException("Invalid article URL");
        }
    }
    private ExternalSource findSourceByHost(
            String articleHost
    ) {
        String normalizedArticleHost = normalizeHost(articleHost);

        List<ExternalSource> sources = sourceRepo.findByIsActiveTrue();
        for (ExternalSource source : sources) {
            if (source.getWebsiteUrl() == null || source.getWebsiteUrl().isBlank()) {
                continue;
            }

            try {
                URI sourceUri = new URI(source.getWebsiteUrl());
                String sourceHost = normalizeHost(sourceUri.getHost());
                if (normalizedArticleHost.equals(sourceHost)
                        || normalizedArticleHost.endsWith("." + sourceHost)) {
                    return source;
                }
            } catch (URISyntaxException ignored) {}
        }

        throw new BadRequestException("Unsupported news source");
    }
    private String normalizeHost(String host) {
        String result = host.toLowerCase();

        if (result.startsWith("www.")) {
            result = result.substring(4);
        }

        return result;
    }
    private Document fetchDocument(String url) {

        try {
            return Jsoup
                    .connect(url)
                    .userAgent("Mozilla/5.0 SmartNews/1.0")
                    .timeout(10000)
                    .followRedirects(true)
                    .get();

        } catch (IOException e) {
            throw new BadRequestException("Unable to fetch article");
        }
    }
    private String getCanonicalUrl(Document document, String originalUrl) {

        Element canonical = document.selectFirst("link[rel=canonical]");

        if (canonical == null) {
            return originalUrl;
        }

        String canonicalUrl = canonical.attr("abs:href");

        if (canonicalUrl == null || canonicalUrl.isBlank()) {
            canonicalUrl = canonical.attr("href");
        }

        if (canonicalUrl == null || canonicalUrl.isBlank()) {
            return originalUrl;
        }
        return canonicalUrl.trim();
    }
    private String extractTitle(Document document) {

        String title =
                extractMeta(document, "meta[property=og:title]", "content");

        if (title == null || title.isBlank()) {
            title = document.title();
        }

        if (title == null || title.isBlank()) {
            throw new BadRequestException("Article title could not be detected");
        }

        if (title.length() > 500) {
            title = title.substring(0, 500);
        }
        return title.trim();
    }
    private String extractMeta(Document document, String selector, String attribute) {
        Element element = document.selectFirst(selector);

        if (element == null) {
            return null;
        }
        String value = element.attr(attribute);

        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
    private String extractAuthor(Document document) {
        String author = extractMeta(document, "meta[name=author]", "content");

        if (author == null) {
            author = extractMeta(document, "meta[property=article:author]", "content");
        }

        if (author != null && author.length() > 255) {
            author = author.substring(0, 255);
        }
        return author;
    }
    private String extractCategory(Document document) {
        String category = extractMeta(document, "meta[property=article:section]", "content");

        if (category == null) {
            category = extractMeta(document, "meta[name=section]", "content");
        }

        if (category != null && category.length() > 100) {
            category = category.substring(0, 100);
        }
        return category;
    }
    private LocalDateTime extractPublishedAt(Document document) {
        String value =
                extractMeta(document, "meta[property=article:published_time]", "content");
        if (value == null) {
            value = extractMeta(document, "meta[name=pubdate]", "content");
        }
        if (value == null) {
            return null;
        }
        try {
            return OffsetDateTime
                    .parse(value)
                    .toLocalDateTime();
        } catch (DateTimeParseException ignored) {}
        try {
            return LocalDateTime.parse(value);
        } catch (DateTimeParseException ignored) {}
        return null;
    }
}