package com.example.smartnews.service.impl;

import com.example.smartnews.dto.request.CreateTagRequest;
import com.example.smartnews.dto.request.UpdateTagRequest;
import com.example.smartnews.dto.response.TagResponse;
import com.example.smartnews.entity.Tag;
import com.example.smartnews.exception.ResourceNotFoundException;
import com.example.smartnews.repository.TagRepository;
import com.example.smartnews.service.TagService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    @Transactional
    public TagResponse create(CreateTagRequest request) {
        Tag tag = new Tag();
        tag.setTagName(request.getTagName());
        Tag saved = tagRepository.save(tag);
        return mapToResponse(saved);
    }

    @Override
    public TagResponse getById(Integer id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found"));
        return mapToResponse(tag);
    }

    @Override
    public List<TagResponse> getAll() {
        return tagRepository.findAll()
                .stream().map(this::mapToResponse).toList();
    }

    @Override
    @Transactional
    public TagResponse update(Integer id, UpdateTagRequest request) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found"));
        tag.setTagName(request.getTagName());
        return mapToResponse(tag);
    }

    @Override
    public void delete(Integer id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found"));
        tagRepository.delete(tag);
    }

    private TagResponse mapToResponse(Tag tag) {
        TagResponse response = new TagResponse();
        response.setTagId(tag.getTagId());
        response.setTagName(tag.getTagName());
        return response;
    }
}
