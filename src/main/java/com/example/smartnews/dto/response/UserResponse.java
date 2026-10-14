package com.example.smartnews.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {
    private Integer accountId;
    private String email;
    private String name;
    private String role;
    private Boolean status;
    private Boolean verified;
    private String provider;
}
