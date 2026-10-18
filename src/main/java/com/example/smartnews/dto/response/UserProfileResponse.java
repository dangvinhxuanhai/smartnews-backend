package com.example.smartnews.dto.response;

import com.example.smartnews.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserProfileResponse {
    private String email;
    private String name;
    private UserRole role;
}
