package com.vention.authorization_service.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FileServiceImplTest {

    @InjectMocks
    private FileServiceImpl fileService;

    @Mock
    private MultipartFile mockFile;

    @Mock
    private AmazonS3 amazonS3;

    @Value("${cloud.aws.s3bucket.url}")
    private String filePath;

    @Test
    public void testUploadFileSuccess() throws IOException {
        String filename = "testFile.txt";
        PutObjectResult s3Result = mock();
        byte[] bytes = {1, 2, 3, 5};
        when(mockFile.getOriginalFilename()).thenReturn(filename);
        doReturn(s3Result).when(amazonS3).putObject(any());
        doReturn(bytes).when(mockFile).getBytes();
        String expectedPath = filePath + filename; // Replace with the actual file path

        String result = fileService.uploadFile(mockFile);

        verify(mockFile, times(2)).getOriginalFilename();
        verify(mockFile, times(1)).getBytes();
        verify(amazonS3, times(1)).putObject(any());
        assertEquals(expectedPath, result);
    }
}
