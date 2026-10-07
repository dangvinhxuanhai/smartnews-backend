package com.example.smartnews.dto.request;

import com.example.smartnews.enums.ArticleStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ArticleSearchRequest {
    private String keyword;
    private Integer categoryId;
    private Integer authorId;
    private ArticleStatus status;
    private Integer tagId;

    private LocalDateTime fromDate;
    private LocalDateTime toDate;

    private String sortBy;

    private Integer page = 0;
    private Integer size = 10;
}
