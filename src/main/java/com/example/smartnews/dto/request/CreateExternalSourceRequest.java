package com.example.smartnews.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateExternalSourceRequest {

    @NotBlank(message = "Source code is required")
    @Size(
            max = 50,
            message = "Source code must not exceed 50 characters"
    )
    private String code;

    @NotBlank(message = "Source name is required")
    @Size(
            max = 150,
            message = "Source name must not exceed 150 characters"
    )
    private String name;

    @Size(max = 500)
    private String websiteUrl;

    @Size(max = 500)
    private String rssUrl;

    @Size(max = 500)
    private String logoUrl;

    @Size(max = 10)
    private String countryCode;

    @Size(max = 10)
    private String languageCode;
}
