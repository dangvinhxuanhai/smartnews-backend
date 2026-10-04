package com.example.smartnews.controller;

import com.example.smartnews.dto.request.CreateCategoryRequest;
import com.example.smartnews.dto.request.UpdateCategoryRequest;
import com.example.smartnews.dto.response.CategoryResponse;
import com.example.smartnews.entity.Category;
import com.example.smartnews.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    @GetMapping
    private List<CategoryResponse>  getAll(){
        return categoryService.getAll();
    }
    @GetMapping("/{id}")
    public CategoryResponse getById(
            @PathVariable Integer id) {

        return categoryService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('Admin')")
    public CategoryResponse create(@Valid @RequestBody CreateCategoryRequest request) {

        return categoryService.create(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('Admin')")
    public CategoryResponse update(
            @PathVariable Integer id,
            @Valid
            @RequestBody UpdateCategoryRequest request) {

        return categoryService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('Admin')")
    public void delete(
            @PathVariable Integer id) {

        categoryService.delete(id);
    }
}
