package com.example.smartnews.service;

import org.apache.commons.lang3.ClassUtils;
import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
    String uploadFile(MultipartFile file);
}
