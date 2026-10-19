package com.example.smartnews.repository;

import com.example.smartnews.entity.ExternalArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ExternalArticleRepository
        extends JpaRepository<ExternalArticle, Integer>,
        JpaSpecificationExecutor<ExternalArticle> {

    boolean existsByUrl(String url);

    boolean existsBySource_SourceIdAndSourceArticleKey(
            Integer sourceId,
            String sourceArticleKey
    );
}
