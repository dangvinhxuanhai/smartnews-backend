package com.example.smartnews.controller;

import com.example.smartnews.dto.request.ArticleSearchRequest;
import com.example.smartnews.dto.request.CreateArticleRequest;
import com.example.smartnews.dto.request.UpdateArticleRequest;
import com.example.smartnews.dto.response.ArticleResponse;
import com.example.smartnews.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping
    public ArticleResponse create(
            @RequestBody CreateArticleRequest request
    ) {
        System.out.println("CREATE ARTICLE RUNNING");
        return articleService.create(request);
    }
    @GetMapping
    public Page<ArticleResponse> getAll(
            @RequestParam(defaultValue = "0")
            int page,
            @RequestParam(defaultValue = "5")
            int size
    ) {
        return articleService.getPublishedArticles(page, size);
    }
    @PutMapping("/{id}")
    public ArticleResponse update(
            @PathVariable Integer id,
            @RequestBody UpdateArticleRequest request
    ) {
        return articleService.update(id, request);
    }
    @DeleteMapping("/{id}")
    public String delete(
            @PathVariable Integer id
    ) {
        articleService.delete(id);
        return "Deleted successfully";
    }
    @GetMapping("/search")
    public Page<ArticleResponse> search(ArticleSearchRequest request){
        return articleService.search(request);
    }

    @GetMapping("/{id}")
    public ArticleResponse getById(
            @PathVariable Integer id
    ) {
        return articleService.getById(id);
    }
}
