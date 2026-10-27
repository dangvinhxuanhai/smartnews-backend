package com.example.smartnews.dto.request;

import com.example.smartnews.enums.StoryStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StorySearchRequest {

    private String keyword;

    private StoryStatus status;

    private Integer categoryId;

    @Min(value = 0)
    private Integer page = 0;

    @Min(value = 1)
    @Max(value = 100)
    private Integer size = 10;

    private String sortBy = "newest";
}