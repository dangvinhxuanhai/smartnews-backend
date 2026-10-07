package com.example.smartnews.dto.request;

import com.example.smartnews.enums.ArticleStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class CreateArticleRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String content;
    private String imageUrl;
    private Integer categoryId;
    private List<Integer> tagIds;
}
