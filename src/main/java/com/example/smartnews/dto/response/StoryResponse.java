package com.example.smartnews.dto.response;

import com.example.smartnews.enums.StoryStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class StoryResponse {

    private Integer storyId;

    private String title;

    private String summary;

    private String heroImageUrl;

    private StoryStatus status;

    private Integer categoryId;

    private String categoryName;

    private Integer createdById;

    private String createdByName;

    private Integer viewCount;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    private LocalDateTime publishedDate;

    private List<String> tags;

    private List<StorySourceResponse> sources;

    private List<StoryInternalArticleResponse> smartNewsArticles;
}
