package com.vention.authorization_service.service.impl;

import com.vention.authorization_service.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Value("${files.image-path}")
    private String fileDirectory;

    @Override
    public String uploadFile(MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(fileDirectory + file.getOriginalFilename());
            Files.write(path, bytes);
            return path.toString();
        } catch (IOException e) {
            log.warn(e.getMessage());
            return "Failed to upload '" + file.getOriginalFilename() + "'";
        }
    }
}