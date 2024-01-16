package com.vention.authorization_service.service;

import com.vention.authorization_service.domain.UserEntity;
import com.vention.authorization_service.domain.UserState;
import com.vention.authorization_service.dto.request.UserProfileFillRequestDTO;
import com.vention.authorization_service.dto.request.UserUpdateRequestDTO;
import com.vention.authorization_service.dto.response.CourierResponseDTO;
import com.vention.authorization_service.dto.response.UserResponseDTO;
import com.vention.authorization_service.dto.response.UserUpdateResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    UserEntity saveUser(UserEntity user);

    UserEntity getByEmail(String email);

    boolean isEligibleForRegistration(String email);

    UserUpdateResponseDTO fillProfile(UserProfileFillRequestDTO request);

    String uploadProfilePicture(Long userId, MultipartFile file);

    UserUpdateResponseDTO updateUser(UserUpdateRequestDTO dto);

    void deleteUser(Long userId);

    UserResponseDTO getById(Long id);
  
    UserEntity getByUsername(String username);

    List<CourierResponseDTO> getAllByCarType(String carType);

    List<Long> getUsersIdList(boolean isCourier);

    List<String> getAllAdminEmails();

    Long getAllActiveUsers(UserState userState);
}
