package com.example.tasks.controller.services;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class S3Service {
    private final S3Client s3Client;
    
    @Value("${aws.s3.bucketName}")
    private String bucketName;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadFile(MultipartFile file) throws IOException {
        String key = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        
        s3Client.putObject(PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build(), 
                RequestBody.fromBytes(file.getBytes()));
        return "https://" + bucketName + ".s3.amazonaws.com/" + key;
    }
}
