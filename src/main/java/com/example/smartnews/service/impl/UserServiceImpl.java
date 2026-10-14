package com.example.smartnews.service.impl;

import com.example.smartnews.dto.request.UserSearchRequest;
import com.example.smartnews.dto.response.UserResponse;
import com.example.smartnews.entity.SystemAccount;
import com.example.smartnews.exception.ResourceNotFoundException;
import com.example.smartnews.repository.SystemAccountRepository;
import com.example.smartnews.service.UserService;
import com.example.smartnews.specification.UserSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final SystemAccountRepository accountRepo;

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> search(
            UserSearchRequest request
    ) {

        Specification<SystemAccount> spec =
                Specification
                        .where(UserSpecification.hasKeyword(request.getKeyword()))
                        .and(UserSpecification.hasRole(request.getRole()))
                        .and(UserSpecification.hasStatus(request.getStatus()));

        Sort.Direction direction =
                "asc".equalsIgnoreCase(request.getSortDirection()) ? Sort.Direction.ASC : Sort.Direction.DESC;

        Sort sort = Sort.by(direction, request.getSortBy());

        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                sort
        );

        return accountRepo.findAll(spec, pageable)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getById(Integer id) {

        SystemAccount account = accountRepo.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return mapToResponse(account);
    }

    @Override
    @Transactional
    public UserResponse changeStatus(
            Integer id,
            boolean status
    ) {

        SystemAccount account =
                accountRepo.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        account.setStatus(status);

        return mapToResponse(account);
    }

    private UserResponse mapToResponse(
            SystemAccount account
    ) {

        return UserResponse.builder()
                .accountId(account.getAccountId())
                .email(account.getEmail())
                .name(account.getName())
                .role(account.getRole())
                .status(account.getStatus())
                .verified(account.getIsVerified())
                .provider(account.getProvider())
                .build();
    }
}
