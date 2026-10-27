package com.example.smartnews.repository;

import com.example.smartnews.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StoryRepository
        extends JpaRepository<Story, Integer>,
        JpaSpecificationExecutor<Story> {
}
