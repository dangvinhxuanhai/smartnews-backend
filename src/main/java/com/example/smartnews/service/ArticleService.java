package com.example.smartnews.service;

import com.example.smartnews.dto.request.ArticleSearchRequest;
import com.example.smartnews.dto.request.CreateArticleRequest;
import com.example.smartnews.dto.request.UpdateArticleRequest;
import com.example.smartnews.dto.response.ArticleResponse;
import org.springframework.data.domain.Page;

public interface ArticleService {
    ArticleResponse create(CreateArticleRequest request);
    Page<ArticleResponse> getPublishedArticles(int page, int size);
    ArticleResponse update(
            Integer articleId,
            UpdateArticleRequest request
    );
    void delete(Integer articleId);
    ArticleResponse getById(Integer articleId);
    Page<ArticleResponse> search(ArticleSearchRequest request, int page, int size);
}
