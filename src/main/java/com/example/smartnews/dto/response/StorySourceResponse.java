package com.example.smartnews.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class StorySourceResponse {

    private Integer externalArticleId;

    private String title;

    private String sourceName;

    private String url;

    private LocalDateTime publishedAt;
}