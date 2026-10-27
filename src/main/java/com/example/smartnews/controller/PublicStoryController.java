package com.example.smartnews.controller;

import com.example.smartnews.dto.request.PublicStorySearchRequest;
import com.example.smartnews.dto.response.PublicStoryDetailResponse;
import com.example.smartnews.dto.response.PublicStoryResponse;
import com.example.smartnews.service.PublicStoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/stories")
@RequiredArgsConstructor
public class PublicStoryController {

    private final PublicStoryService storyService;


    @GetMapping
    public ResponseEntity<Page<PublicStoryResponse>> search(
            @Valid @ModelAttribute
            PublicStorySearchRequest request
    ) {

        return ResponseEntity.ok(
                storyService.search(request)
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<PublicStoryDetailResponse> getById(
            @PathVariable Integer id
    ) {

        return ResponseEntity.ok(
                storyService.getById(id)
        );
    }
}
