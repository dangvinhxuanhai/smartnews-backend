package com.example.smartnews.controller;

import com.example.smartnews.dto.request.ArticleSearchRequest;
import com.example.smartnews.dto.response.ArticleResponse;
import com.example.smartnews.service.ArticleService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/articles")
public class PublicArticleController {
    private final ArticleService articleService;

    public PublicArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }
    @GetMapping
    public ResponseEntity<Page<ArticleResponse>> getArticles(
            @Valid @ModelAttribute ArticleSearchRequest request
    ) {
        return ResponseEntity.ok(
                articleService.getPublishedArticles(request)
        );
    }
    @GetMapping("/{id}")
    public ResponseEntity<ArticleResponse> getArticleById(
            @PathVariable Integer id
    ) {
        return ResponseEntity.ok(
                articleService.getPublishedArticlesById(id)
        );
    }
}
