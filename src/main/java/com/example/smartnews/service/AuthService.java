package com.example.smartnews.service;


import com.example.smartnews.dto.request.LoginRequest;
import com.example.smartnews.dto.request.RegisterRequest;
import com.example.smartnews.dto.response.UserProfileResponse;

public interface AuthService {
    void register(RegisterRequest request);
    String login(LoginRequest request);
    UserProfileResponse getCurrentUser();
    void verifyAccount(String token);
}
