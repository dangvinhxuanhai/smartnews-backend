package com.example.smartnews.dto.response;

import com.example.smartnews.enums.ArticleStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StoryInternalArticleResponse {

    private Integer articleId;

    private String title;

    private ArticleStatus status;

    private String authorName;
}