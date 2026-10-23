package com.example.smartnews.dto.request;

import com.example.smartnews.enums.ExternalArticleReviewStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewExternalArticleRequest {

    @NotNull(message = "Review status is required")
    private ExternalArticleReviewStatus reviewStatus;

    private Integer categoryId;

    @Size(
            max = 1000,
            message = "Editor note must not exceed 1000 characters"
    )
    private String editorNote;
}
