package com.example.smartnews.service.impl;

import com.cloudinary.Cloudinary;
import com.example.smartnews.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {
    @Autowired
    private Cloudinary cloud;
    @Override
    public String uploadFile(MultipartFile file) {
        try{
            Map<String, Object> options = Map.of(
                    "folder",
                    "smartnews/articles"
            );

            Map uploadResult = cloud.uploader().upload(file.getBytes(), options);
            return uploadResult.get("secure_url").toString();
        }catch (Exception e){
            throw new RuntimeException(
                    "Upload image failed"
            );
        }
    }
}
