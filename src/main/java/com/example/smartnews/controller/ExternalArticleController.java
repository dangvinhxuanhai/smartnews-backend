package com.example.smartnews.controller;

import com.example.smartnews.dto.request.ExternalArticleSearchRequest;
import com.example.smartnews.dto.response.ExternalArticleDetailResponse;
import com.example.smartnews.dto.response.ExternalArticleResponse;
import com.example.smartnews.service.ExternalArticleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/external-articles")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('Admin', 'Staff')")
public class ExternalArticleController {
    private final ExternalArticleService articleService;
    @GetMapping
    public ResponseEntity<Page<ExternalArticleResponse>> search(
            @Valid @ModelAttribute ExternalArticleSearchRequest request
    ) {
        return ResponseEntity.ok(articleService.search(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExternalArticleDetailResponse> getById(
            @PathVariable Integer id
    ) {
        return ResponseEntity.ok(articleService.getById(id));
    }
}
