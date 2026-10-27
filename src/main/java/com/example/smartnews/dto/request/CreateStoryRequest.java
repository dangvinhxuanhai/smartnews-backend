package com.example.smartnews.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class CreateStoryRequest {

    @NotBlank(message = "Story title is required")
    @Size(
            max = 255,
            message = "Story title must not exceed 255 characters"
    )
    private String title;

    private String summary;

    @Size(
            max = 700,
            message = "Hero image URL must not exceed 700 characters"
    )
    private String heroImageUrl;

    private Integer categoryId;

    private Set<Integer> tagIds;
}
