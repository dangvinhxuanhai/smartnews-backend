package com.example.smartnews.dto.response;

import com.example.smartnews.enums.ExternalArticleReviewStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ExternalArticleDetailResponse {

    private Integer externalArticleId;

    private String sourceArticleKey;

    private String title;

    private String summary;

    private String content;

    private String url;

    private String imageUrl;

    private String author;

    private LocalDateTime publishedAt;

    private LocalDateTime importedAt;

    private String originalCategory;

    private Integer categoryId;

    private String categoryName;

    private Integer sourceId;

    private String sourceCode;

    private String sourceName;

    private ExternalArticleReviewStatus reviewStatus;

    private Integer reviewedById;

    private String reviewedByName;

    private LocalDateTime reviewedAt;

    private String editorNote;
}