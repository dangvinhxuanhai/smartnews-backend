package com.example.smartnews.dto.request;

import com.example.smartnews.enums.ArticleStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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

    @Min(
            value = 0,
            message = "Page must be greater than or equal to 0"
    )
    private Integer page = 0;

    @Min(
            value = 1,
            message = "Size must be at least 1"
    )
    @Max(
            value = 100,
            message = "Size must not exceed 100"
    )
    private Integer size = 10;
}
