package com.example.smartnews.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UpdateStoryRequest {

    @NotBlank(message = "Story title is required")
    @Size(max = 255)
    private String title;

    private String summary;

    @Size(max = 700)
    private String heroImageUrl;

    private Integer categoryId;

    private Set<Integer> tagIds;
}
