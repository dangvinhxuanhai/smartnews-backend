package com.example.smartnews.repository;

import com.example.smartnews.entity.SystemAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface SystemAccountRepository extends JpaRepository<SystemAccount,Integer>,
        JpaSpecificationExecutor<SystemAccount> {
    Optional<SystemAccount> findByEmail(String email);
    Optional<SystemAccount> findByVerificationToken(String token);
    long countByStatus(Boolean status);
}
