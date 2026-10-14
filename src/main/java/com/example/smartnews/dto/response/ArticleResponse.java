package com.example.smartnews.dto.response;

import com.example.smartnews.enums.ArticleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Builder
@AllArgsConstructor
public class ArticleResponse {
    private Integer articleId;
    private String title;
    private String content;
    private ArticleStatus status;
    private String imageUrl;
    private Integer viewCount;
    private String authorName;
    private String categoryName;
    private LocalDateTime createdDate;
    private List<String> tags;
}
