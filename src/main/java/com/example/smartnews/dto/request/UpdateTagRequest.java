package com.example.smartnews.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateTagRequest {

    @NotBlank(message = "Tag name is required")
    private String tagName;
}
