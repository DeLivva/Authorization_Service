package com.vention.authorization_service.service.impl;

import com.vention.authorization_service.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileServiceImpl implements FileService {

    public static final String LOCAL_FILE_PATH = "C:/Users/User/Desktop/";

    @Override
    public String uploadFile(MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(LOCAL_FILE_PATH + file.getOriginalFilename());
            Files.write(path, bytes);
            return path.toString();
        } catch (IOException e) {
            return "Failed to upload '" + file.getOriginalFilename() + "'";
        }
    }
}
