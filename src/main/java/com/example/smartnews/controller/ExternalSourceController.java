package com.example.smartnews.controller;

import com.example.smartnews.dto.request.CreateExternalSourceRequest;
import com.example.smartnews.dto.request.UpdateExternalSourceRequest;
import com.example.smartnews.dto.request.UpdateExternalSourceStatusRequest;
import com.example.smartnews.dto.response.ExternalSourceResponse;
import com.example.smartnews.service.ExternalSourceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/external-sources")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('Admin', 'Staff')")
public class ExternalSourceController {

    private final ExternalSourceService sourceService;


    @GetMapping
    public ResponseEntity<List<ExternalSourceResponse>> getAll() {

        return ResponseEntity.ok(sourceService.getAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<ExternalSourceResponse> getById(
            @PathVariable Integer id
    ) {

        return ResponseEntity.ok(sourceService.getById(id));
    }


    @PostMapping
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<ExternalSourceResponse> create(
            @Valid @RequestBody CreateExternalSourceRequest request
    ) {

        return ResponseEntity.ok(sourceService.create(request));
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<ExternalSourceResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateExternalSourceRequest request
    ) {

        return ResponseEntity.ok(sourceService.update(id, request));
    }


    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<ExternalSourceResponse> changeStatus(
            @PathVariable Integer id,
            @Valid @RequestBody
            UpdateExternalSourceStatusRequest request
    ) {
        return ResponseEntity.ok(sourceService.changeStatus(id, request.getActive()));
    }
}
