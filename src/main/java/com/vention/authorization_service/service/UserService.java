package com.vention.authorization_service.service;

import com.vention.authorization_service.domain.UserEntity;
import com.vention.authorization_service.dto.request.UserUpdateRequestDTO;
import com.vention.authorization_service.dto.response.UserUpdateResponseDTO;

public interface UserService {

    UserEntity saveUser(UserEntity user);

    UserEntity getUserByEmail(String email);

    boolean isEmailUnique(String email);

    UserUpdateResponseDTO updateUser(UserUpdateRequestDTO dto);

    void deleteUser(Long userId);
}
