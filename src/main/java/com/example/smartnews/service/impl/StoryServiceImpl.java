package com.example.smartnews.service.impl;

import com.example.smartnews.dto.request.CreateStoryRequest;
import com.example.smartnews.dto.request.StorySearchRequest;
import com.example.smartnews.dto.request.UpdateStoryRequest;
import com.example.smartnews.dto.request.UpdateStoryStatusRequest;
import com.example.smartnews.dto.response.StoryInternalArticleResponse;
import com.example.smartnews.dto.response.StoryResponse;
import com.example.smartnews.dto.response.StorySourceResponse;
import com.example.smartnews.entity.*;
import com.example.smartnews.enums.ExternalArticleReviewStatus;
import com.example.smartnews.enums.StoryStatus;
import com.example.smartnews.exception.BadRequestException;
import com.example.smartnews.exception.ResourceNotFoundException;
import com.example.smartnews.repository.*;
import com.example.smartnews.service.StoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

import static com.example.smartnews.specification.StorySpecification.*;

@Service
@RequiredArgsConstructor
public class StoryServiceImpl
        implements StoryService {

    private final StoryRepository storyRepo;

    private final ExternalArticleRepository externalArticleRepo;

    private final NewsArticleRepository newsArticleRepo;

    private final CategoryRepository categoryRepo;

    private final TagRepository tagRepo;

    private final SystemAccountRepository accountRepo;

    @Override
    @Transactional(readOnly = true)
    public Page<StoryResponse> search(
            StorySearchRequest request
    ) {

        Specification<Story> spec =
                Specification.where(hasKeyword(request.getKeyword()))
                        .and(hasStatus(request.getStatus()))
                        .and(hasCategory(request.getCategoryId()));

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), buildSort(request.getSortBy()));

        return storyRepo
                .findAll(spec, pageable)
                .map(this::mapToResponse);
    }

    private Sort buildSort(String sortBy) {
        if ("oldest".equals(sortBy)) {
            return Sort.by("createdDate").ascending();
        }
        if ("mostViewed".equals(sortBy)) {
            return Sort.by("viewCount").descending();
        }
        return Sort.by("createdDate").descending();
    }

    @Override
    @Transactional(readOnly = true)
    public StoryResponse getById(Integer id) {
        return mapToResponse(getStory(id));
    }

    @Override
    @Transactional
    public StoryResponse create(CreateStoryRequest request) {

        Story story = new Story();
        story.setTitle(request.getTitle().trim());
        story.setSummary(request.getSummary());
        story.setHeroImageUrl(request.getHeroImageUrl());

        if (request.getCategoryId() != null) {
            Category category = categoryRepo
                            .findById(request.getCategoryId())
                            .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            story.setCategory(category);
        }
        if (request.getTagIds() != null) {
            story.setTags(new HashSet<>(tagRepo.findAllById(request.getTagIds())));
        }

        String email = SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        SystemAccount currentUser = accountRepo.findByEmail(email)
                        .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));
        story.setCreatedBy(currentUser);

        Story saved = storyRepo.save(story);
        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public StoryResponse update(Integer id, UpdateStoryRequest request) {

        Story story = getStory(id);
        story.setTitle(request.getTitle().trim());
        story.setSummary(request.getSummary());
        story.setHeroImageUrl(request.getHeroImageUrl());


        if (request.getCategoryId() == null) {
            story.setCategory(null);
        } else {
            Category category = categoryRepo.findById(request.getCategoryId())
                            .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            story.setCategory(category);
        }

        if (request.getTagIds() != null) {
            story.setTags(new HashSet<>(tagRepo.findAllById(request.getTagIds())));
        } else {
            story.getTags().clear();
        }

        return mapToResponse(story);
    }

    @Override
    @Transactional
    public StoryResponse changeStatus(Integer id, UpdateStoryStatusRequest request) {
        Story story = getStory(id);
        if (request.getStatus() == StoryStatus.Published) {

            validateStoryForPublish(story);

            if (story.getPublishedDate() == null) {
                story.setPublishedDate(LocalDateTime.now());
            }
        }
        story.setStatus(request.getStatus());
        return mapToResponse(story);
    }
    private void validateStoryForPublish(Story story) {
        if (story.getExternalArticles().size() < 2) {
            throw new BadRequestException("A story requires at least two external articles");
        }
        long sourceCount = story.getExternalArticles()
                        .stream()
                        .map(article -> article.getSource().getSourceId())
                        .distinct()
                        .count();
        if (sourceCount < 2) {
            throw new BadRequestException("A story requires articles from at least two different sources");
        }
    }

    @Override
    @Transactional
    public StoryResponse addExternalArticle(Integer storyId, Integer externalArticleId) {

        Story story = getStory(storyId);
        ExternalArticle article = externalArticleRepo.findById(externalArticleId)
                        .orElseThrow(() -> new ResourceNotFoundException("External article not found"));

        if (article.getReviewStatus() != ExternalArticleReviewStatus.Reviewed) {
            throw new BadRequestException(
                    "External article must be reviewed before adding it to a story"
            );
        }


        boolean alreadyAdded = story.getExternalArticles()
                        .stream()
                        .anyMatch(a -> a.getExternalArticleId().equals(externalArticleId));

        if (alreadyAdded) {
            throw new BadRequestException(
                    "External article is already in this story"
            );
        }
        story.getExternalArticles().add(article);
        return mapToResponse(story);
    }

    @Override
    @Transactional
    public StoryResponse removeExternalArticle(Integer storyId, Integer externalArticleId) {
        Story story = getStory(storyId);

        boolean removed = story.getExternalArticles()
                .removeIf(article -> article.getExternalArticleId().equals(externalArticleId));

        if (!removed) {
            throw new ResourceNotFoundException(
                    "External article is not in this story"
            );
        }
        return mapToResponse(story);
    }

    @Override
    @Transactional
    public StoryResponse addNewsArticle(Integer storyId, Integer articleId) {

        Story story = getStory(storyId);

        NewsArticle article = newsArticleRepo
                        .findById(articleId)
                        .orElseThrow(() -> new ResourceNotFoundException("Article not found"));


        boolean alreadyAdded = story.getNewsArticles()
                        .stream()
                        .anyMatch(a -> a.getArticleId().equals(articleId));

        if (alreadyAdded) {
            throw new BadRequestException(
                    "Article is already in this story"
            );
        }
        story.getNewsArticles().add(article);
        return mapToResponse(story);
    }

    @Override
    @Transactional
    public StoryResponse removeNewsArticle(Integer storyId, Integer articleId) {
        Story story = getStory(storyId);

        boolean removed = story.getNewsArticles()
                .removeIf(article -> article.getArticleId().equals(articleId));

        if (!removed) {
            throw new ResourceNotFoundException(
                    "Article is not in this story"
            );
        }
        return mapToResponse(story);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        Story story = getStory(id);
        storyRepo.delete(story);
    }

    private Story getStory(Integer id) {
        return storyRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Story not found"));
    }

    private StoryResponse mapToResponse(Story story) {

        List<StorySourceResponse> sources = story.getExternalArticles()
                        .stream()
                        .map(article ->
                                StorySourceResponse.builder()
                                        .externalArticleId(article.getExternalArticleId())
                                        .title(article.getTitle())
                                        .sourceName(article.getSource().getName())
                                        .url(article.getUrl())
                                        .publishedAt(article.getPublishedAt())
                                        .build()
                        )
                        .toList();


        List<StoryInternalArticleResponse> internalArticles =
                story.getNewsArticles()
                        .stream()
                        .map(article ->
                                StoryInternalArticleResponse.builder()
                                        .articleId(article.getArticleId())
                                        .title(article.getTitle())
                                        .status(article.getStatus())
                                        .authorName(article.getAuthor().getName())
                                        .build()
                        )
                        .toList();


        return StoryResponse.builder()
                .storyId(story.getStoryId())
                .title(story.getTitle())
                .summary(story.getSummary())
                .heroImageUrl(story.getHeroImageUrl())
                .status(story.getStatus())
                .categoryId(story.getCategory() == null
                                ? null
                                : story.getCategory()
                                .getCategoryId())
                .categoryName(story.getCategory() == null
                                ? null
                                : story.getCategory()
                                .getCategoryName())
                .createdById(story.getCreatedBy().getAccountId())
                .createdByName(story.getCreatedBy().getName())
                .viewCount(story.getViewCount())
                .createdDate(story.getCreatedDate())
                .updatedDate(story.getUpdatedDate())
                .publishedDate(story.getPublishedDate())
                .tags(story.getTags()
                                .stream()
                                .map(Tag::getTagName)
                                .toList())
                .sources(sources)
                .smartNewsArticles(internalArticles)
                .build();
    }
}
