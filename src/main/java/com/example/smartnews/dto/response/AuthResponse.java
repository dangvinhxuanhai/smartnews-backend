package com.example.smartnews.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
@AllArgsConstructor
public class AuthResponse {

    private String message;
}
