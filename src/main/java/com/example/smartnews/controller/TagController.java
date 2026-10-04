package com.example.smartnews.controller;

import com.example.smartnews.dto.request.CreateTagRequest;
import com.example.smartnews.dto.request.UpdateTagRequest;
import com.example.smartnews.dto.response.TagResponse;
import com.example.smartnews.service.TagService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tag")
public class TagController {
    private final TagService service;

    public TagController(TagService service) {
        this.service = service;
    }
    @GetMapping
    public List<TagResponse> getAll() {
        return service.getAll();
    }
    @GetMapping("/{id}")
    public TagResponse getById(
            @PathVariable Integer id
    ){
        return service.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('Admin')")
    public TagResponse create(
            @Valid
            @RequestBody CreateTagRequest request
    ){
        return service.create(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('Admin')")
    public TagResponse update(
            @PathVariable Integer id,
            @Valid
            @RequestBody UpdateTagRequest request) {

        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('Admin')")
    public void delete(
            @PathVariable Integer id) {

        service.delete(id);
    }
}
