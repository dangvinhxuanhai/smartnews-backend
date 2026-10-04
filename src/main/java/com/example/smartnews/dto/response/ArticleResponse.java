package com.example.smartnews.dto.response;

import com.example.smartnews.enums.ArticalStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ArticleResponse {
    private Integer articleId;
    private String title;
    private String content;
    private ArticalStatus status;
    private String imageUrl;
    private Integer viewCount;
    private String authorName;
    private String categoryName;
    private LocalDateTime createdDate;
    private List<String> tags;
}
