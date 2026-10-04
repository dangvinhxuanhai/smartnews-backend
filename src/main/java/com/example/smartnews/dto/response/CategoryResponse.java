package com.example.smartnews.dto.response;

import lombok.Data;

@Data
public class CategoryResponse {

    private Integer categoryId;
    private String categoryName;
    private Integer parentId;
    private String parentName;
    private Boolean isActive;
}
