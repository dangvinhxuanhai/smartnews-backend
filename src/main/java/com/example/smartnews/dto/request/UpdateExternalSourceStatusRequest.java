package com.example.smartnews.dto.request;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateExternalSourceStatusRequest {

    @NotNull(message = "Status is required")
    private Boolean active;
}
