package com.example.smartnews.service.impl;

import com.example.smartnews.dto.response.DashboardResponse;
import com.example.smartnews.enums.ArticleStatus;
import com.example.smartnews.repository.CategoryRepository;
import com.example.smartnews.repository.NewsArticleRepository;
import com.example.smartnews.repository.SystemAccountRepository;
import com.example.smartnews.repository.TagRepository;
import com.example.smartnews.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final NewsArticleRepository articleRepo;
    private final SystemAccountRepository accountRepo;
    private final CategoryRepository categoryRepo;
    private final TagRepository tagRepo;

    @Override
    @Transactional(readOnly = true)
    public DashboardResponse getStatistics() {

        return DashboardResponse.builder()
                .totalArticles(articleRepo.count())
                .publishedArticles(articleRepo.countByStatus(ArticleStatus.Published))
                .draftArticles(articleRepo.countByStatus(ArticleStatus.Draft))
                .totalUsers(accountRepo.count())
                .activeUsers(accountRepo.countByStatus(true))
                .totalCategories(categoryRepo.count())
                .totalTags(tagRepo.count())
                .totalViews(articleRepo.sumTotalViews())
                .build();
    }
}