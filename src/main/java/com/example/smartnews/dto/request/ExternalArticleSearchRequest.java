package com.example.smartnews.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
public class ExternalArticleSearchRequest {

    private String keyword;

    private Integer sourceId;

    private Integer categoryId;

    private String originalCategory;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime fromDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime toDate;

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
    private Integer size = 20;

    private String sortBy = "newest";
}
