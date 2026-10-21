package com.example.smartnews.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImportExternalArticleRequest {

    @NotBlank(message = "URL is required")
    @Size(
            max = 2000,
            message = "URL must not exceed 2000 characters"
    )
    private String url;
}
