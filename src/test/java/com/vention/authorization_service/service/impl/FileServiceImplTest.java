package com.vention.authorization_service.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FileServiceImplTest {

    @InjectMocks
    private FileServiceImpl fileService;

    @Mock
    private MultipartFile mockFile;

    @Value("${files.image-path}")
    private String filePath;

    @Test
    public void testUploadFileSuccess() throws IOException {
        String filename = "testFile.txt";
        byte[] fileContent = "content".getBytes();
        when(mockFile.getBytes()).thenReturn(fileContent);
        when(mockFile.getOriginalFilename()).thenReturn(filename);
        String expectedPath = filePath + filename; // Replace with the actual file path

        String result = fileService.uploadFile(mockFile);

        assertEquals(expectedPath, result);
        Path path = Paths.get(expectedPath);
        verify(mockFile, times(1)).getBytes();
        assertTrue(Files.exists(path));
    }

    @Test
    public void testUploadFileFailure() throws IOException {
        String filename = "testfile.txt";
        when(mockFile.getBytes()).thenThrow(new IOException("Failed to read file"));
        when(mockFile.getOriginalFilename()).thenReturn(filename);

        String result = fileService.uploadFile(mockFile);
        assertEquals("Failed to upload '" + filename + "'", result);
        verify(mockFile, times(1)).getBytes();
    }
}
