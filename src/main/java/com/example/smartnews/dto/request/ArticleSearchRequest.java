package com.example.smartnews.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ArticleSearchRequest {
    private String keyword;
    private Integer categoryId;
    private Integer authorId;
    private String status;

    private LocalDateTime fromDate;
    private LocalDateTime toDate;

    private String sortBy;
}
