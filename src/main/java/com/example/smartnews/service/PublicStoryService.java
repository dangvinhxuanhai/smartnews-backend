package com.example.smartnews.service;

import com.example.smartnews.dto.request.PublicStorySearchRequest;
import com.example.smartnews.dto.response.PublicStoryDetailResponse;
import com.example.smartnews.dto.response.PublicStoryResponse;
import org.springframework.data.domain.Page;

public interface PublicStoryService {

    Page<PublicStoryResponse> search(
            PublicStorySearchRequest request
    );

    PublicStoryDetailResponse getById(
            Integer id
    );
}
