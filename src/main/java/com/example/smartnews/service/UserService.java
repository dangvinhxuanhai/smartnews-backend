package com.example.smartnews.service;

import com.example.smartnews.dto.request.UserSearchRequest;
import com.example.smartnews.dto.response.UserResponse;
import org.springframework.data.domain.Page;

public interface UserService {

    Page<UserResponse> search(UserSearchRequest request);

    UserResponse getById(Integer id);

    UserResponse changeStatus(
            Integer id,
            boolean status
    );
}
