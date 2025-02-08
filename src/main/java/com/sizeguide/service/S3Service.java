package com.sizeguide.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public void uploadTemplate(String templateName, byte[] content, String contentType) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);
            metadata.setContentLength(content.length);

            InputStream inputStream = new ByteArrayInputStream(content);
            PutObjectRequest request = new PutObjectRequest(bucketName, templateName, inputStream, metadata);

            s3Client.putObject(request);
            log.info("Successfully uploaded template: {} to S3", templateName);
        } catch (Exception e) {
            log.error("Failed to upload template: {} to S3", templateName, e);
            throw new RuntimeException("Failed to upload template to S3", e);
        }
    }

    public S3Object downloadTemplate(String templateName) {
        try {
            return s3Client.getObject(bucketName, templateName);
        } catch (Exception e) {
            log.error("Failed to download template: {} from S3", templateName, e);
            throw new RuntimeException("Failed to download template from S3", e);
        }
    }
}
