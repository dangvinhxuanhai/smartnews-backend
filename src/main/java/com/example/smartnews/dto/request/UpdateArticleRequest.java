package com.example.smartnews.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class UpdateArticleRequest {
    private String title;

    private String content;

    private String imageUrl;

    private Integer categoryId;

    private List<Integer> tagIds;
}
