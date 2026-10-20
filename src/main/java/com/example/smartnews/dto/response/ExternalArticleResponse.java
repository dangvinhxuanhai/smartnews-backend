package com.example.smartnews.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ExternalArticleResponse {

    private Integer externalArticleId;

    private String title;

    private String url;

    private String imageUrl;

    private String author;

    private LocalDateTime publishedAt;

    private String originalCategory;

    private Integer categoryId;

    private String categoryName;

    private Integer sourceId;

    private String sourceCode;

    private String sourceName;
}
