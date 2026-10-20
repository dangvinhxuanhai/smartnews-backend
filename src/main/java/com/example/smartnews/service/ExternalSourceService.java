package com.example.smartnews.service;

import com.example.smartnews.dto.request.CreateExternalSourceRequest;
import com.example.smartnews.dto.request.UpdateExternalSourceRequest;
import com.example.smartnews.dto.response.ExternalSourceResponse;

import java.util.List;

public interface ExternalSourceService {

    List<ExternalSourceResponse> getAll();

    ExternalSourceResponse getById(Integer id);

    ExternalSourceResponse create(
            CreateExternalSourceRequest request
    );

    ExternalSourceResponse update(
            Integer id,
            UpdateExternalSourceRequest request
    );

    ExternalSourceResponse changeStatus(
            Integer id,
            boolean active
    );
}
