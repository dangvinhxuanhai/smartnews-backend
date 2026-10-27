package com.example.smartnews.service;

import com.example.smartnews.dto.request.*;
import com.example.smartnews.dto.response.StoryResponse;
import org.springframework.data.domain.Page;

public interface StoryService {

    Page<StoryResponse> search(StorySearchRequest request);

    StoryResponse getById(Integer id);

    StoryResponse create(CreateStoryRequest request);

    StoryResponse update(Integer id, UpdateStoryRequest request);

    StoryResponse changeStatus(Integer id, UpdateStoryStatusRequest request);

    StoryResponse addExternalArticle(Integer storyId, Integer externalArticleId);

    StoryResponse removeExternalArticle(Integer storyId, Integer externalArticleId);

    StoryResponse addNewsArticle(Integer storyId, Integer articleId);

    StoryResponse removeNewsArticle(Integer storyId, Integer articleId);

    void delete(Integer id);
}