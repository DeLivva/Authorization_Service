package com.vention.authorization_service.service.impl;

import com.vention.authorization_service.domain.SecurityCredentialEntity;
import com.vention.authorization_service.domain.UserEntity;
import com.vention.authorization_service.domain.UserState;
import com.vention.authorization_service.dto.request.UserProfileFillRequestDTO;
import com.vention.authorization_service.dto.request.UserUpdateRequestDTO;
import com.vention.authorization_service.dto.response.UserResponseDTO;
import com.vention.authorization_service.dto.response.UserUpdateResponseDTO;
import com.vention.authorization_service.exception.DataNotFoundException;
import com.vention.authorization_service.exception.DuplicateDataException;
import com.vention.authorization_service.exception.InvalidFileTypeException;
import com.vention.authorization_service.exception.LoginFailedException;
import com.vention.authorization_service.mapper.UserMapper;
import com.vention.authorization_service.repository.SecurityCredentialRepository;
import com.vention.authorization_service.repository.UserRepository;
import com.vention.authorization_service.service.FileService;
import com.vention.authorization_service.service.UserService;
import com.vention.authorization_service.utils.FileUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final SecurityCredentialRepository securityCredentialRepository;
    private final FileService fileService;
    private final FileUtils fileUtils;
    private final UserMapper userMapper;

    @Override
    public UserEntity saveUser(UserEntity user) {
        return repository.save(user);
    }

    @Override
    public UserEntity getByEmail(String email) {
        return repository.findByEmail(email).orElseThrow(
                () -> new DataNotFoundException("User not found with email: " + email)
        );
    }

    @Override
    public boolean isEligibleForRegistration(String email) {
        var user = repository.findByEmail(email);
        return user.isEmpty();
    }

    @Override
    @Transactional
    public UserUpdateResponseDTO fillProfile(UserProfileFillRequestDTO request) {
        var user = repository.findById(request.getUserId())
                .orElseThrow(() -> new DataNotFoundException("User not found on id: " + request.getUserId()));

        if (securityCredentialRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new DuplicateDataException("This username already exist: " + request.getUsername());
        }

        if(!user.getUserState().equals(UserState.VERIFIED)){
            throw new LoginFailedException(user.getUserState().name());
        }

        var credentials = user.getCredentials();
        credentials.setUsername(request.getUsername());
        securityCredentialRepository.save(credentials);

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setUserState(UserState.AUTHORIZED);
        repository.save(user);
        return userMapper.mapUserEntityToUpdateResponseDto(user);
    }

    @Override
    public String uploadProfilePicture(Long userId, MultipartFile file) {
        var user = repository.findById(userId).orElseThrow(() -> new DataNotFoundException("User not found"));

        if (!fileUtils.isImageFile(file)) {
            throw new InvalidFileTypeException("The provided file is not an image. Please upload a valid image file.");
        }
        String savedLocation = fileService.uploadFile(file);
        user.setPhoto(savedLocation);
        repository.save(user);
        return savedLocation;
    }

    @Override
    @Transactional
    public UserUpdateResponseDTO updateUser(UserUpdateRequestDTO dto) {
        UserEntity user = repository.findById(dto.getUserId()).orElseThrow(
                () -> new DataNotFoundException("User with this id not found: " + dto.getUserId())
        );

        // change credentials
        SecurityCredentialEntity credentials = user.getCredentials();
        credentials.setUsername(dto.getUsername());
        if (dto.getPassword() != null) {
            credentials.setPassword(dto.getPassword());
        }
        securityCredentialRepository.save(credentials);

        // change user
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        repository.save(user);

        return UserUpdateResponseDTO.builder()
                .username(user.getCredentials().getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .build();
    }

    @Override
    public void deleteUser(Long userId) {
        UserEntity user = repository.findById(userId).orElseThrow(
                () -> new DataNotFoundException("User with this id not found: " + userId)
        );
        user.setUserState(UserState.DELETED);
        repository.save(user);
    }

    @Override
    public UserResponseDTO getById(Long id) {
        var userEntity = repository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("User not fount on id: " + id));
        return userMapper.mapEntityToResponseDto(userEntity);
    }

    @Override
    public UserEntity getByUsername(String username) {
        return repository.findByCredentials_Username(username)
                .orElseThrow(() -> new DataNotFoundException("User not found with username: " + username));
    }
}
