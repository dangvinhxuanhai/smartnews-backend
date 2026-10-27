package com.example.smartnews.controller;

import com.example.smartnews.dto.request.*;
import com.example.smartnews.dto.response.StoryResponse;
import com.example.smartnews.service.StoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stories")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('Admin', 'Staff')")
public class StoryController {
    private final StoryService storyService;

    @GetMapping
    public ResponseEntity<Page<StoryResponse>> search(
            @Valid @ModelAttribute StorySearchRequest request
    ) {
        return ResponseEntity.ok(storyService.search(request));
    }


    @GetMapping("/{id}")
    public ResponseEntity<StoryResponse> getById(
            @PathVariable Integer id
    ) {
        return ResponseEntity.ok(storyService.getById(id));
    }


    @PostMapping
    public ResponseEntity<StoryResponse> create(
            @Valid @RequestBody CreateStoryRequest request
    ) {
        return ResponseEntity.ok(storyService.create(request));
    }


    @PutMapping("/{id}")
    public ResponseEntity<StoryResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateStoryRequest request
    ) {
        return ResponseEntity.ok(storyService.update(id, request));
    }


    @PatchMapping("/{id}/status")
    public ResponseEntity<StoryResponse> changeStatus(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateStoryStatusRequest request
    ) {

        return ResponseEntity.ok(storyService.changeStatus(id, request));
    }


    @PostMapping(
            "/{storyId}/external-articles/{articleId}"
    )
    public ResponseEntity<StoryResponse> addExternalArticle(
            @PathVariable Integer storyId,
            @PathVariable Integer articleId
    ) {
        return ResponseEntity.ok(storyService.addExternalArticle(storyId, articleId));
    }


    @DeleteMapping(
            "/{storyId}/external-articles/{articleId}"
    )
    public ResponseEntity<StoryResponse> removeExternalArticle(
            @PathVariable Integer storyId,
            @PathVariable Integer articleId
    ) {
        return ResponseEntity.ok(storyService.removeExternalArticle(storyId, articleId));
    }


    @PostMapping(
            "/{storyId}/articles/{articleId}"
    )
    public ResponseEntity<StoryResponse> addNewsArticle(
            @PathVariable Integer storyId,
            @PathVariable Integer articleId
    ) {

        return ResponseEntity.ok(storyService.addNewsArticle(storyId, articleId));
    }


    @DeleteMapping(
            "/{storyId}/articles/{articleId}"
    )
    public ResponseEntity<StoryResponse> removeNewsArticle(
            @PathVariable Integer storyId,
            @PathVariable Integer articleId
    ) {
        return ResponseEntity.ok(storyService.removeNewsArticle(storyId, articleId));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Integer id
    ) {
        storyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
