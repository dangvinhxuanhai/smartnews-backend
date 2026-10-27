package com.example.smartnews.repository;

import com.example.smartnews.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StoryRepository
        extends JpaRepository<Story, Integer>,
        JpaSpecificationExecutor<Story> {
    @Modifying
    @Query("""
        UPDATE Story s
        SET s.viewCount = COALESCE(s.viewCount, 0) + 1
        WHERE s.storyId = :id
    """)
    int increaseView(
            @Param("id") Integer id
    );
}
