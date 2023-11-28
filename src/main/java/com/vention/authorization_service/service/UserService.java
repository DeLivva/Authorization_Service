package com.vention.authorization_service.service;

import com.vention.authorization_service.domain.UserEntity;
import com.vention.authorization_service.dto.request.UserProfileFillRequestDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface UserService {

    UserEntity saveUser(UserEntity user);

    Optional<UserEntity> getByEmail(String email);

    boolean isEmailUnique(String email);

    UserEntity fillProfile(UserProfileFillRequestDTO request);

    String uploadProfilePicture(Long userId, MultipartFile file);
}
