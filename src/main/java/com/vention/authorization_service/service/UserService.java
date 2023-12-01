package com.vention.authorization_service.service;

import com.vention.authorization_service.domain.UserEntity;
import com.vention.authorization_service.dto.request.UserProfileFillRequestDTO;
import org.springframework.web.multipart.MultipartFile;
import com.vention.authorization_service.dto.request.UserUpdateRequestDTO;
import com.vention.authorization_service.dto.response.UserUpdateResponseDTO;

public interface UserService {

    UserEntity saveUser(UserEntity user);

    UserEntity getByEmail(String email);

    boolean isEligibleForRegistration(String email);

    UserEntity fillProfile(UserProfileFillRequestDTO request);

    String uploadProfilePicture(Long userId, MultipartFile file);

    UserUpdateResponseDTO updateUser(UserUpdateRequestDTO dto);

    void deleteUser(Long userId);
}
