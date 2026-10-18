package com.example.smartnews.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateTagRequest {

    @NotBlank(message = "Tag name is required")
    @Size(
            max = 50,
            message = "Tag name must not exceed 50 characters"
    )
    private String tagName;
}