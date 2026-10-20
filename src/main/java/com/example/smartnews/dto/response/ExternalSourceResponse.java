package com.example.smartnews.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ExternalSourceResponse {

    private Integer sourceId;

    private String code;

    private String name;

    private String websiteUrl;

    private String rssUrl;

    private String logoUrl;

    private String countryCode;

    private String languageCode;

    private Boolean active;

    private LocalDateTime createdDate;
}
