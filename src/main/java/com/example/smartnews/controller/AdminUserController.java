package com.example.smartnews.controller;

import com.example.smartnews.dto.request.UpdateUserStatusRequest;
import com.example.smartnews.dto.request.UserSearchRequest;
import com.example.smartnews.dto.response.UserResponse;
import com.example.smartnews.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('Admin')")
public class AdminUserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserResponse>> search(
            @ModelAttribute UserSearchRequest request
    ) {
        return ResponseEntity.ok(userService.search(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(
            @PathVariable Integer id
    ) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<UserResponse> changeStatus(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateUserStatusRequest request
    ) {
        return ResponseEntity.ok(
                userService.changeStatus(id, request.getStatus())
        );
    }
}
