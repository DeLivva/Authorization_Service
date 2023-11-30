package com.vention.authorization_service.utils;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileUtilsTest {
    private FileUtils fileUtils;

    @BeforeEach
    public void setUp() {
        fileUtils = new FileUtils();
    }

    @Test
    void testNonImageFile() {
        MultipartFile nonImageFile = new MockMultipartFile("file", "filename.txt", "text/plain", new byte[1]);
        assertFalse(fileUtils.isImageFile(nonImageFile));
    }

    @Test
    void testImageFile() {
        MultipartFile imageFile = new MockMultipartFile("file", "filename.jpg", "image/jpeg", new byte[1]);
        assertTrue(fileUtils.isImageFile(imageFile));
    }
}