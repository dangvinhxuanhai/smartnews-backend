package com.example.smartnews.service.impl;

import com.example.smartnews.dto.request.LoginRequest;
import com.example.smartnews.dto.request.RegisterRequest;
import com.example.smartnews.dto.response.UserProfileResponse;
import com.example.smartnews.entity.SystemAccount;
import com.example.smartnews.exception.ForbiddenException;
import com.example.smartnews.repository.SystemAccountRepository;
import com.example.smartnews.security.JwtUtil;
import com.example.smartnews.service.AuthService;
import com.example.smartnews.service.EmailService;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private SystemAccountRepository repo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private EmailService emailService;
    @Override
    public void register(RegisterRequest request) {
        if(repo.findByEmail(request.getEmail()).isPresent()){
            throw new ForbiddenException("Email already exists");
        }
        SystemAccount account = new SystemAccount();
        account.setEmail(request.getEmail());
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setName(request.getName());
        account.setRole("Staff");
        account.setStatus(true);
        account.setIsVerified(false);
        account.setProvider("LOCAL");
        account.setVerificationToken(UUID.randomUUID().toString());
        repo.save(account);
        emailService.sendVerificationEmail(
                account.getEmail(),
                account.getVerificationToken()
        );
    }
    @Override
    public String login(LoginRequest request) {

        SystemAccount user = repo.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email"));

        if (!Boolean.TRUE.equals(
                user.getIsVerified()
        )) {

            throw new RuntimeException(
                    "Account not verified"
            );
        }

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {
            throw new ForbiddenException("Wrong password");
        }
        return jwtUtil.generateToken(user.getEmail());
    }

    @Override
    public UserProfileResponse getCurrentUser() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String email = authentication.getName();

        SystemAccount user = repo.findByEmail(email)
                .orElseThrow(() -> new ForbiddenException("User not found"));

        return new UserProfileResponse(
                user.getEmail(),
                user.getName(),
                user.getRole());
    }

    @Override
    public void verifyAccount(String token) {
        SystemAccount user= repo.findByVerificationToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));
        user.setIsVerified(true);
        user.setVerificationToken(null);
        repo.save(user);
    }
}
