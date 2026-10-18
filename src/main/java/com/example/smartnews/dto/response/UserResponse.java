package com.example.smartnews.dto.response;

import com.example.smartnews.enums.UserRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {
    private Integer accountId;
    private String email;
    private String name;
    private UserRole role;
    private Boolean status;
    private Boolean verified;
    private String provider;
}
