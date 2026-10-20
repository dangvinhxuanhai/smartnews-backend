package com.example.smartnews.service;

import com.example.smartnews.dto.request.ExternalArticleSearchRequest;
import com.example.smartnews.dto.response.ExternalArticleDetailResponse;
import com.example.smartnews.dto.response.ExternalArticleResponse;
import org.springframework.data.domain.Page;

public interface ExternalArticleService {

    Page<ExternalArticleResponse> search(
            ExternalArticleSearchRequest request
    );

    ExternalArticleDetailResponse getById(
            Integer id
    );
}
