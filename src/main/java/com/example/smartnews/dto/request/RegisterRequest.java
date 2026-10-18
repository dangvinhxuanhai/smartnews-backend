package com.example.smartnews.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @Email(message = "Email is valid")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(
            min=6,
            max = 100,
            message = "Password must be between 6 and 100 characters"
    )
    private String password;

    @NotBlank(message = "Name is required")
    @Size(
            max = 100,
            message = "Name must not exceed 100 characters"
    )
    private String name;
}
