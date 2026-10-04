package com.example.smartnews.service;

public interface EmailService {

    void sendVerificationEmail(String to, String token);
}