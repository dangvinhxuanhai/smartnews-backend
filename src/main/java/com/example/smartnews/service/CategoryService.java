package com.example.smartnews.service;

import com.example.smartnews.dto.request.CreateCategoryRequest;
import com.example.smartnews.dto.request.UpdateCategoryRequest;
import com.example.smartnews.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse create (CreateCategoryRequest request);
    CategoryResponse getById (Integer id);
    List<CategoryResponse> getAll();
    CategoryResponse update(Integer id, UpdateCategoryRequest request);
    void delete (Integer id);
}
