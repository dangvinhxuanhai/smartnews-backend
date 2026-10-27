package com.example.smartnews.dto.request;

import com.example.smartnews.enums.StoryStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStoryStatusRequest {

    @NotNull(message = "Story status is required")
    private StoryStatus status;
}