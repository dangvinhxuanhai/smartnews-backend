package com.example.smartnews.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublicStorySearchRequest {

    private String keyword;

    private Integer categoryId;

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

    private String sortBy = "newest";
}
