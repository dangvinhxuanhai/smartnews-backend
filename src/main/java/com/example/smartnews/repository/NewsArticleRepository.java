package com.example.smartnews.repository;

import com.example.smartnews.entity.NewsArticle;
import com.example.smartnews.enums.ArticalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface NewsArticleRepository
        extends JpaRepository<NewsArticle,Integer>,
        JpaSpecificationExecutor<NewsArticle> {
    Page<NewsArticle> findByStatus(ArticalStatus status, Pageable pageable);
    @Modifying
    @Query("""
        UPDATE NewsArticle a
        SET a.viewCount = COALESCE(a.viewCount, 0) + 1
        WHERE a.articleId = :id
    """)
    int increaseView(@Param("id") Integer id);
}
