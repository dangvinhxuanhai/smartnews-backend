package com.example.smartnews.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

    @GetMapping("/profile")
    @PreAuthorize("hasAnyRole('Admin', 'Staff')")
    public String profile() {

        return "Welcome Staff";
    }
}