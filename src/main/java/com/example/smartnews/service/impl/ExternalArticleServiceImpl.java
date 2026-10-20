package com.example.smartnews.service.impl;

import com.example.smartnews.dto.request.ExternalArticleSearchRequest;
import com.example.smartnews.dto.response.ExternalArticleDetailResponse;
import com.example.smartnews.dto.response.ExternalArticleResponse;
import com.example.smartnews.entity.ExternalArticle;
import com.example.smartnews.exception.ResourceNotFoundException;
import com.example.smartnews.repository.ExternalArticleRepository;
import com.example.smartnews.service.ExternalArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.smartnews.specification.ExternalArticleSpecification.*;

@Service
@RequiredArgsConstructor
public class ExternalArticleServiceImpl
        implements ExternalArticleService {

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
}