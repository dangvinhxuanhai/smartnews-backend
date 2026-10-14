package com.example.smartnews.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSearchRequest {
    private String keyword;
    private String role;
    private Boolean status;

    private Integer page = 0;
    private Integer size = 10;

    private String sortBy = "accountId";
    private String sortDirection = "desc";
}
