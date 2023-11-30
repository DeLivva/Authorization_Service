package com.vention.authorization_service.controller;

import com.vention.authorization_service.domain.UserEntity;
import com.vention.authorization_service.dto.request.UserProfileFillRequestDTO;
import com.vention.authorization_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping
    public ResponseEntity<UserEntity> registerUser(@Valid @RequestBody UserProfileFillRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.fillProfile(request));
    }

    @PostMapping("/picture/{user-id}")
    public ResponseEntity<String> uploadProfilePic(@PathVariable("user-id") Long userId,
                                                   @RequestParam("image") MultipartFile file) {
        String link = service.uploadProfilePicture(userId, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(link);
    }
}
