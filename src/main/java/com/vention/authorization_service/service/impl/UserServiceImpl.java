package com.vention.authorization_service.service.impl;

import com.vention.authorization_service.domain.SecurityCredentialEntity;
import com.vention.authorization_service.domain.UserEntity;
import com.vention.authorization_service.dto.request.UserProfileFillRequestDTO;
import com.vention.authorization_service.dto.request.UserUpdateRequestDTO;
import com.vention.authorization_service.dto.response.UserUpdateResponseDTO;
import com.vention.authorization_service.exception.DataNotFoundException;
import com.vention.authorization_service.exception.DuplicateDataException;
import com.vention.authorization_service.repository.SecurityCredentialRepository;
import com.vention.authorization_service.exception.InvalidFileTypeException;
import com.vention.authorization_service.repository.UserRepository;
import com.vention.authorization_service.service.FileService;
import com.vention.authorization_service.service.UserService;
import com.vention.authorization_service.utils.FileUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final SecurityCredentialRepository securityCredentialRepository;
    private final FileService fileService;
    private final FileUtils fileUtils;

    @Override
    public UserEntity saveUser(UserEntity user) {
        return repository.save(user);
    }

    @Override
    public UserEntity getByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException("User not found with email: " + email));
    }

    @Override
    public boolean isEligibleForRegistration(String email) {
        var user = repository.findByEmail(email);
        return user.isEmpty() || user.get().getIsDeleted();
    }

    @Override
    public UserEntity fillProfile(UserProfileFillRequestDTO request) {
        var user = repository.findById(request.getUserId())
                .orElseThrow(() -> new DataNotFoundException("User not found on id: " + request.getUserId()));

        if (securityCredentialRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new DuplicateDataException("This username already exist: " + request.getUsername());
        }
        var credentials = user.getCredentials();
        credentials.setUsername(request.getUsername());
        securityCredentialRepository.save(credentials);

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        repository.save(user);
        return user;
    }

    @Override
    public String uploadProfilePicture(Long userId, MultipartFile file) {
        var user = repository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

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
        UserEntity user = userRepository.findById(dto.getUserId()).orElseThrow(
                () -> new DataNotFoundException("User with this id not found: " + dto.getUserId())
        );

        Optional<UserEntity> byEmail = userRepository.findByEmail(dto.getEmail());
        if (byEmail.isPresent() && !Objects.equals(byEmail.get().getId(), user.getId())) {
            throw new DuplicateDataException("This email already exists: " + dto.getEmail());
        }
        // change credentials
        SecurityCredentialEntity credentials = user.getCredentials();
        credentials.setUsername(dto.getUsername());
        if (dto.getPassword() != null) {
            credentials.setPassword(dto.getPassword());
        }
        credentials.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        securityCredentialRepository.save(credentials);

        // change user
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        userRepository.save(user);

        return UserUpdateResponseDTO.builder()
                .username(user.getCredentials().getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .build();
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(
                () -> new DataNotFoundException("User with this id not found: " + userId)
        );
        userRepository.delete(user);
        securityCredentialRepository.delete(user.getCredentials());
    }
}
