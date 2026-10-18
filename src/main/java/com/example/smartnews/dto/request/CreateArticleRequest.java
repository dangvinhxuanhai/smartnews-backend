package com.example.smartnews.dto.request;

import com.example.smartnews.enums.ArticleStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CreateArticleRequest {
    @NotBlank(message = "Title is required")
    @Size(
            max = 255,
            message = "Title must not exceed 255 characters"
    )
    private String title;

    @NotBlank(message = "Content is required")
    private String content;
    @Size(
            max = 500,
            message = "Image URL must not exceed 500 characters"
    )
    private String imageUrl;

    @NotNull(message = "Category is required")
    private Integer categoryId;

    private List<Integer> tagIds;
}
