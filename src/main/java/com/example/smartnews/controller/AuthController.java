package com.example.smartnews.controller;

import com.example.smartnews.dto.request.LoginRequest;
import com.example.smartnews.dto.request.RegisterRequest;
import com.example.smartnews.dto.response.AuthResponse;
import com.example.smartnews.dto.response.LoginResponse;
import com.example.smartnews.dto.response.UserProfileResponse;
import com.example.smartnews.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest request){
        authService.register(request);
        return new AuthResponse("Register success");
    }
    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody LoginRequest request
    ) {

        String token = authService.login(request);

        return new LoginResponse(token);
    }
    @GetMapping("/me")
    public UserProfileResponse me() {

        return authService.getCurrentUser();
    }

    @GetMapping("/verify")
    public String verify(
            @RequestParam String token
    ) {

        authService.verifyAccount(token);

        return "Account verified successfully";
    }

}
