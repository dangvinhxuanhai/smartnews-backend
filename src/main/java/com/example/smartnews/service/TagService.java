package com.example.smartnews.service;

import com.example.smartnews.dto.request.CreateTagRequest;
import com.example.smartnews.dto.request.UpdateTagRequest;
import com.example.smartnews.dto.response.TagResponse;

import java.util.List;

public interface TagService {

    TagResponse create(CreateTagRequest request);

    TagResponse getById(Integer id);

    List<TagResponse> getAll();

    TagResponse update(
            Integer id,
            UpdateTagRequest request
    );

    void delete(Integer id);
}
