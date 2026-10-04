package com.example.smartnews.service.impl;
import com.example.smartnews.service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl
        implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendVerificationEmail(
            String to,
            String token
    ) {

        String verifyLink =
                "http://localhost:8080/api/auth/verify?token="
                        + token;

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(to);

        message.setSubject(
                "Verify your SmartNews account"
        );

        message.setText(
                "Click this link to verify:\n"
                        + verifyLink
        );

        mailSender.send(message);
    }
}