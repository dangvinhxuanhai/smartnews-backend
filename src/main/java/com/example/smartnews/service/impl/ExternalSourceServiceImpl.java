package com.example.smartnews.service.impl;

import com.example.smartnews.dto.request.CreateExternalSourceRequest;
import com.example.smartnews.dto.request.UpdateExternalSourceRequest;
import com.example.smartnews.dto.response.ExternalSourceResponse;
import com.example.smartnews.entity.ExternalSource;
import com.example.smartnews.exception.BadRequestException;
import com.example.smartnews.exception.ResourceNotFoundException;
import com.example.smartnews.repository.ExternalSourceRepository;
import com.example.smartnews.service.ExternalSourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExternalSourceServiceImpl
        implements ExternalSourceService {

    private final ExternalSourceRepository sourceRepo;


    @Override
    @Transactional(readOnly = true)
    public List<ExternalSourceResponse> getAll() {

        return sourceRepo.findAll(Sort.by("name").ascending())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public ExternalSourceResponse getById(Integer id) {

        ExternalSource source = sourceRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("External source not found"));

        return mapToResponse(source);
    }


    @Override
    @Transactional
    public ExternalSourceResponse create(
            CreateExternalSourceRequest request
    ) {

        String code = request.getCode().trim().toLowerCase();

        String name = request.getName().trim();

        if (sourceRepo.existsByCode(code)) {
            throw new BadRequestException("Source code already exists");
        }

        if (sourceRepo.existsByName(name)) {
            throw new BadRequestException("Source name already exists");
        }

        ExternalSource source = new ExternalSource();

        source.setCode(code);
        source.setName(name);
        source.setWebsiteUrl(request.getWebsiteUrl());
        source.setRssUrl(request.getRssUrl());
        source.setLogoUrl(request.getLogoUrl());
        source.setCountryCode(request.getCountryCode());
        source.setLanguageCode(request.getLanguageCode());

        source.setIsActive(true);
        source.setCreatedDate(LocalDateTime.now());

        ExternalSource saved = sourceRepo.save(source);

        return mapToResponse(saved);
    }


    @Override
    @Transactional
    public ExternalSourceResponse update(Integer id, UpdateExternalSourceRequest request) {

        ExternalSource source = sourceRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("External source not found"));

        String name = request.getName().trim();

        if (sourceRepo.existsByNameAndSourceIdNot(name, id)) {
            throw new BadRequestException("Source name already exists");
        }

        source.setName(name);
        source.setWebsiteUrl(request.getWebsiteUrl());
        source.setRssUrl(request.getRssUrl());
        source.setLogoUrl(request.getLogoUrl());
        source.setCountryCode(request.getCountryCode());
        source.setLanguageCode(request.getLanguageCode());

        return mapToResponse(source);
    }


    @Override
    @Transactional
    public ExternalSourceResponse changeStatus(Integer id, boolean active) {

        ExternalSource source = sourceRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("External source not found"));

        source.setIsActive(active);
        return mapToResponse(source);
    }


    private ExternalSourceResponse mapToResponse(
            ExternalSource source
    ) {
        return ExternalSourceResponse.builder()
                .sourceId(source.getSourceId())
                .code(source.getCode())
                .name(source.getName())
                .websiteUrl(source.getWebsiteUrl())
                .rssUrl(source.getRssUrl())
                .logoUrl(source.getLogoUrl())
                .countryCode(source.getCountryCode())
                .languageCode(source.getLanguageCode())
                .active(source.getIsActive())
                .createdDate(source.getCreatedDate())
                .build();
    }
}
