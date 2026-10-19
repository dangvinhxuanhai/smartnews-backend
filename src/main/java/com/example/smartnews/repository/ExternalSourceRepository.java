package com.example.smartnews.repository;

import com.example.smartnews.entity.ExternalSource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExternalSourceRepository
        extends JpaRepository<ExternalSource, Integer> {

    Optional<ExternalSource> findByCode(String code);

    boolean existsByCode(String code);
}