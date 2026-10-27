package com.example.smartnews.service.impl;

import com.example.smartnews.dto.request.PublicStorySearchRequest;
import com.example.smartnews.dto.response.*;
import com.example.smartnews.entity.Story;
import com.example.smartnews.entity.Tag;
import com.example.smartnews.enums.ArticleStatus;
import com.example.smartnews.enums.ExternalArticleReviewStatus;
import com.example.smartnews.enums.StoryStatus;
import com.example.smartnews.exception.ResourceNotFoundException;
import com.example.smartnews.repository.StoryRepository;
import com.example.smartnews.service.PublicStoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.smartnews.specification.StorySpecification.*;

@Service
@RequiredArgsConstructor
public class PublicStoryServiceImpl
        implements PublicStoryService {

    private final StoryRepository storyRepo;

    @Override
    @Transactional(readOnly = true)
    public Page<PublicStoryResponse> search(PublicStorySearchRequest request) {

        Specification<Story> spec =
                Specification.where(hasStatus(StoryStatus.Published))
                        .and(hasKeyword(request.getKeyword()))
                        .and(hasCategory(request.getCategoryId()));

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), buildSort(request.getSortBy()));

        return storyRepo
                .findAll(spec, pageable)
                .map(this::mapToPublicResponse);
    }

    private Sort buildSort(String sortBy) {
        if ("mostViewed".equalsIgnoreCase(sortBy)) {
            return Sort.by("viewCount").descending();
        }

        if ("oldest".equalsIgnoreCase(sortBy)) {
            return Sort.by("publishedDate").ascending();
        }

        return Sort.by("publishedDate").descending();
    }

    private PublicStoryResponse mapToPublicResponse(Story story) {
        List<String> sourceNames =
                story.getExternalArticles()
                        .stream()
                        .filter(article -> article.getReviewStatus() == ExternalArticleReviewStatus.Reviewed)
                        .map(article -> article.getSource().getName())
                        .distinct()
                        .toList();


        return PublicStoryResponse.builder()
                .storyId(story.getStoryId())
                .title(story.getTitle())
                .summary(story.getSummary())
                .heroImageUrl(story.getHeroImageUrl())
                .categoryId(story.getCategory() == null ? null : story.getCategory().getCategoryId())
                .categoryName(story.getCategory() == null ? null : story.getCategory().getCategoryName())
                .viewCount(story.getViewCount())
                .publishedDate(story.getPublishedDate())
                .tags(story.getTags().stream().map(Tag::getTagName).toList())
                .sourceCount(sourceNames.size())
                .sourceNames(sourceNames)
                .build();
    }

    @Override
    @Transactional
    public PublicStoryDetailResponse getById(Integer id) {
        Story story = storyRepo.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Story not found"));

        if (story.getStatus() != StoryStatus.Published) {
            throw new ResourceNotFoundException("Story not found");
        }

        storyRepo.increaseView(id);

        return mapToPublicDetail(story, story.getViewCount() + 1);
    }

    private PublicStoryDetailResponse mapToPublicDetail(Story story, Integer currentViewCount) {
        List<StorySourceResponse> sources = story.getExternalArticles()
                .stream()
                .filter(article -> article.getReviewStatus() == ExternalArticleReviewStatus.Reviewed)
                .map(article -> StorySourceResponse.builder()
                        .externalArticleId(article.getExternalArticleId())
                        .title(article.getTitle())
                        .sourceName(article.getSource().getName())
                        .url(article.getUrl())
                        .publishedAt(article.getPublishedAt())
                        .build()
                )
                .toList();
        List<StoryInternalArticleResponse> smartNewsArticles = story.getNewsArticles()
                        .stream()
                        .filter(article -> article.getStatus() == ArticleStatus.Published)
                        .map(article -> StoryInternalArticleResponse.builder()
                                .articleId(article.getArticleId())
                                .title(article.getTitle())
                                .status(article.getStatus())
                                .authorName(article.getAuthor().getName())
                                .build()
                        )
                        .toList();
        return PublicStoryDetailResponse.builder()
                .storyId(story.getStoryId())
                .title(story.getTitle())
                .summary(story.getSummary())
                .heroImageUrl(story.getHeroImageUrl())
                .categoryId(story.getCategory() == null ? null : story.getCategory().getCategoryId())
                .categoryName(story.getCategory() == null ? null : story.getCategory().getCategoryName())
                .viewCount(currentViewCount)
                .publishedDate(story.getPublishedDate())
                .tags(story.getTags().stream().map(Tag::getTagName).toList())
                .sources(sources)
                .smartNewsArticles(smartNewsArticles)
                .build();
    }
}
