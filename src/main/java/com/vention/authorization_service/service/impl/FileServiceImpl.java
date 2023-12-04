package com.vention.authorization_service.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.vention.authorization_service.exception.FileConvertingException;
import com.vention.authorization_service.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    @Value("${cloud.aws.s3bucket.name}")
    private String bucketName;

    @Value("${cloud.aws.s3bucket.url}")
    private String bucketUrl;

    private final AmazonS3 amazonS3;

    @Override
    public String uploadFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        File convertedFile = convertMultipartFileToFile(file);
        amazonS3.putObject(new PutObjectRequest(bucketName, fileName, convertedFile));
        return bucketUrl + fileName;
    }

    private File convertMultipartFileToFile(MultipartFile multipartFile) {
        File convertedFile = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        try (FileOutputStream outputStream = new FileOutputStream(convertedFile)) {
            outputStream.write(multipartFile.getBytes());
            return convertedFile;
        } catch(IOException e) {
            throw new FileConvertingException(e.getMessage());
        }
    }
}