package com.example.smartnews.repository;

import com.example.smartnews.entity.NewsArticle;
import com.example.smartnews.enums.ArticleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface NewsArticleRepository
        extends JpaRepository<NewsArticle,Integer>,
        JpaSpecificationExecutor<NewsArticle> {
    Page<NewsArticle> findByStatus(ArticleStatus status, Pageable pageable);
    Optional<NewsArticle> findByArticleIdAndStatus(Integer articleId, ArticleStatus status);
    @Modifying
    @Query("""
        UPDATE NewsArticle a
        SET a.viewCount = COALESCE(a.viewCount, 0) + 1
        WHERE a.articleId = :id
    """)
    int increaseView(@Param("id") Integer id);
    long countByStatus(ArticleStatus status);
    @Query("""
        SELECT COALESCE(SUM(a.viewCount), 0)
        FROM NewsArticle a
    """)
    Long sumTotalViews();
}
