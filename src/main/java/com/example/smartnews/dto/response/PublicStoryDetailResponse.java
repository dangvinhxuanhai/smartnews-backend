package com.example.smartnews.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class PublicStoryDetailResponse {

    private Integer storyId;

    private String title;

    private String summary;

    private String heroImageUrl;

    private Integer categoryId;

    private String categoryName;

    private Integer viewCount;

    private LocalDateTime publishedDate;

    private List<String> tags;

    private List<StorySourceResponse> sources;

    private List<StoryInternalArticleResponse>
            smartNewsArticles;
}