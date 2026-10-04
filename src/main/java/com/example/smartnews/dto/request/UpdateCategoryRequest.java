package com.example.smartnews.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateCategoryRequest {

    @NotBlank(message = "Category name is required")
    private String categoryName;

    private Integer parentId;

    private Boolean isActive;
}
