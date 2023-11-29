package com.vention.authorization_service.service;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileServiceTest {

    @Test
    void testNonImageFile() {
        MultipartFile nonImageFile = new MockMultipartFile("file", "filename.txt", "text/plain", new byte[1]);
        assertFalse(FileService.isImageFile(nonImageFile));
    }

    @Test
    void testImageFile() {
        MultipartFile imageFile = new MockMultipartFile("file", "filename.jpg", "image/jpeg", new byte[1]);
        assertTrue(FileService.isImageFile(imageFile));
    }
}