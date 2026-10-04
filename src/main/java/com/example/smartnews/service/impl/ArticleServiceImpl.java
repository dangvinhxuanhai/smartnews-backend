package com.example.smartnews.service.impl;

import com.example.smartnews.dto.request.ArticleSearchRequest;
import com.example.smartnews.dto.request.CreateArticleRequest;
import com.example.smartnews.dto.request.UpdateArticleRequest;
import com.example.smartnews.dto.response.ArticleResponse;
import com.example.smartnews.entity.Category;
import com.example.smartnews.entity.NewsArticle;
import com.example.smartnews.entity.SystemAccount;
import com.example.smartnews.entity.Tag;
import com.example.smartnews.enums.ArticalStatus;
import com.example.smartnews.exception.ForbiddenException;
import com.example.smartnews.exception.ResourceNotFoundException;
import com.example.smartnews.repository.CategoryRepository;
import com.example.smartnews.repository.NewsArticleRepository;
import com.example.smartnews.repository.SystemAccountRepository;
import com.example.smartnews.repository.TagRepository;
import com.example.smartnews.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import  static  com.example.smartnews.specification.NewsArticleSpecification.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private NewsArticleRepository articleRepo;

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private TagRepository tagRepo;

    @Autowired
    private SystemAccountRepository userRepo;
    @Override
    @Transactional
    public ArticleResponse create(CreateArticleRequest request) {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String email = authentication.getName();
        SystemAccount author = userRepo.findByEmail(email).orElseThrow();

        Category category = categoryRepo.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        List<Tag> tags = new ArrayList<>();
        if(request.getTagIds() != null && request.getTagIds().isEmpty()) {
            tags = tagRepo.findAllById(request.getTagIds());
            if(tags.size() != request.getTagIds().size()){
                throw new ResourceNotFoundException("One or more tags do not exist");
            }
        }

        NewsArticle article = new NewsArticle();

        article.setTitle(request.getTitle());

        article.setContent(request.getContent());

        article.setStatus(ArticalStatus.Draft);

        article.setImageUrl(request.getImageUrl());

        article.setCategory(category);

        article.setAuthor(author);

        article.setTags(tags);

        article.setCreatedDate(LocalDateTime.now());

        article.setViewCount(0);

        return mapToResponse(article);
    }

    @Override
    public Page<ArticleResponse> getPublishedArticles(int page, int size)
    {

        Pageable pageable = PageRequest.of(page,size,Sort.by("createdDate").descending());

        Page<NewsArticle> articles = articleRepo.findByStatus(ArticalStatus.Published,pageable);

        return articles.map(this::mapToResponse);
    }

    private ArticleResponse mapToResponse(
            NewsArticle article
    ) {

        List<String> tagNames = article.getTags()
                        .stream()
                        .map(Tag::getTagName)
                        .toList();

        return new ArticleResponse(
                article.getArticleId(),
                article.getTitle(),
                article.getContent(),
                article.getStatus(),
                article.getImageUrl(),
                article.getViewCount(),
                article.getAuthor().getName(),
                article.getCategory().getCategoryName(),
                article.getCreatedDate(),
                tagNames
        );
    }
    @Override
    @Transactional
    public ArticleResponse update(
            Integer articleId,
            UpdateArticleRequest request
    ) {

        Authentication auth = SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = auth.getName();

        SystemAccount currentUser = userRepo.findByEmail(email).orElseThrow();

        NewsArticle article = articleRepo.findById(articleId)
                        .orElseThrow(() -> new ResourceNotFoundException("Article not found"));

        // OWNERSHIP CHECK
        boolean isAdmin = currentUser.getRole().equals("Admin");

        boolean isOwner = article.getAuthor()
                        .getAccountId()
                        .equals(currentUser.getAccountId());

        if (!isAdmin && !isOwner) {
            throw new ForbiddenException("You cannot edit this article");
        }

        Category category = categoryRepo.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        List<Tag> tags = tagRepo.findAllById( request.getTagIds() );

        article.setTitle(request.getTitle());

        article.setContent(request.getContent());

        article.setImageUrl(request.getImageUrl());

        article.setCategory(category);

        article.setTags(tags);

        article.setUpdatedDate(LocalDateTime.now());

        return mapToResponse(article);
    }
    @Override
    public void delete(Integer articleId) {

        Authentication auth = SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = auth.getName();

        SystemAccount currentUser = userRepo.findByEmail(email).orElseThrow();

        NewsArticle article = articleRepo.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));

        boolean isAdmin = currentUser.getRole().equals("Admin");

        boolean isOwner = article.getAuthor()
                        .getAccountId()
                        .equals(currentUser.getAccountId());

        if (!isAdmin && !isOwner) {
            throw new ForbiddenException("You cannot delete this article");
        }

        articleRepo.delete(article);
    }

    @Override
    public Page<ArticleResponse> search(ArticleSearchRequest request, int page, int size) {
        Specification<NewsArticle> spe = Specification
                .where(hasKeyword(request.getKeyword()))
                .and(hashCategory(request.getCategoryId()))
                .and(hashAuthor(request.getAuthorId()))
                .and(hashStatus(request.getStatus()))
                .and(createdAfter(request.getFromDate()))
                .and(createdBefore(request.getToDate()));
        Sort sort = buildSort(request.getSortBy());
        Pageable pageable = PageRequest.of(page,size,Sort.by("createdDate").descending());
        Page<NewsArticle> result = articleRepo.findAll(spe,pageable);
        return result.map(this::mapToResponse);
    }

    private Sort buildSort(String sortBy) {
        if(sortBy == null || sortBy.isBlank()){
            return Sort.by("createdDate").descending();
        }
        return switch (sortBy){
            case "oldest" -> Sort.by("createdDate").ascending();
            case "mostViewed" -> Sort.by("viewCount").descending();
            case "recentlyUpdate" -> Sort.by("UpdatedDate").descending();
            default -> Sort.by("createdDate").descending();
        };
    }

    @Override
    @Transactional
    public ArticleResponse getById(Integer articleId) {

        int updated = articleRepo.increaseView(articleId);

        if (updated == 0) {
            throw new ResourceNotFoundException("Article not found");
        }

        NewsArticle article = articleRepo.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));

        return mapToResponse(article);
    }
}
