package com.example.smartnews.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DashboardResponse {

    private long totalArticles;
    private long publishedArticles;
    private long draftArticles;

    private long totalUsers;
    private long activeUsers;

    private long totalCategories;
    private long totalTags;

    private long totalViews;
}