package com.example.smartnews.service.impl;

import com.example.smartnews.dto.request.CreateCategoryRequest;
import com.example.smartnews.dto.request.UpdateCategoryRequest;
import com.example.smartnews.dto.response.CategoryResponse;
import com.example.smartnews.entity.Category;
import com.example.smartnews.exception.ResourceNotFoundException;
import com.example.smartnews.repository.CategoryRepository;
import com.example.smartnews.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepo;

    public CategoryServiceImpl(CategoryRepository categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    @Override
    @Transactional
    public CategoryResponse create(CreateCategoryRequest request) {
        Category parent = null;
        if(request.getParentId() == null){
            parent = categoryRepo.findById(request.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("parent category not found"));
        }
        Category category = new Category();
        category.setCategoryName(request.getCategoryName());
        category.setParent(parent);
        category.setIsActive(true);

        Category saved = categoryRepo.save(category);
        return mapToResponse(saved);
    }

    @Override
    public CategoryResponse getById(Integer id) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categpry not found"));
        return mapToResponse(category);
    }

    @Override
    public List<CategoryResponse> getAll() {
         return categoryRepo.findAll()
                 .stream()
                 .map(this::mapToResponse)
                 .toList();
    }

    @Override
    @Transactional
    public CategoryResponse update(Integer id, UpdateCategoryRequest request) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        Category parent = null;
        if (request.getParentId() != null) {
            if (id.equals(request.getParentId())) {
                throw new IllegalStateException("Category cannot be its own parent");
            }
            parent = categoryRepo.findById(request.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent category not found"));
        }
        category.setCategoryName(request.getCategoryName());

        category.setParent(parent);

        if (request.getIsActive() != null) {
            category.setIsActive(request.getIsActive());
        }

        return mapToResponse(category);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Category not found"));
        category.setIsActive(false);
    }

    private CategoryResponse mapToResponse(
            Category category) {

        CategoryResponse response =
                new CategoryResponse();

        response.setCategoryId(
                category.getCategoryId()
        );

        response.setCategoryName(
                category.getCategoryName()
        );

        response.setIsActive(
                category.getIsActive()
        );

        if (category.getParent() != null) {

            response.setParentId(
                    category.getParent()
                            .getCategoryId()
            );

            response.setParentName(
                    category.getParent()
                            .getCategoryName()
            );
        }

        return response;
    }
}
