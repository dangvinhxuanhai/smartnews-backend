package com.example.smartnews.dto.request;

import com.example.smartnews.enums.UserRole;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSearchRequest {
    private String keyword;
    private UserRole role;
    private Boolean status;

    @Min(
            value = 0,
            message = "Page must be greater than or equal to 0"
    )
    private Integer page = 0;
    @Min(
            value = 1,
            message = "Size must be at least 1"
    )
    @Max(
            value = 100,
            message = "Size must not exceed 100"
    )
    private Integer size = 10;

    @Pattern(
            regexp = "accountId|name|email",
            message = "Invalid sort field"
    )
    private String sortBy = "accountId";
    @Pattern(
            regexp = "(?i)asc|desc",
            message = "Sort direction must be asc or desc"
    )
    private String sortDirection = "desc";
}
