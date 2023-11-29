package com.vention.authorization_service.service.impl;

import com.vention.authorization_service.domain.SecurityCredentialEntity;
import com.vention.authorization_service.domain.UserEntity;
import com.vention.authorization_service.dto.request.UserUpdateRequestDTO;
import com.vention.authorization_service.dto.response.UserUpdateResponseDTO;
import com.vention.authorization_service.exception.DataNotFoundException;
import com.vention.authorization_service.exception.DuplicateDataException;
import com.vention.authorization_service.repository.SecurityCredentialRepository;
import com.vention.authorization_service.repository.UserRepository;
import com.vention.authorization_service.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final SecurityCredentialRepository securityCredentialRepository;

    @Override
    public UserEntity saveUser(UserEntity user) {
        return userRepository.save(user);
    }

    @Override
    public UserEntity getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new DataNotFoundException("Email not found")
        );
    }

    @Override
    public boolean isEmailUnique(String email) {
        return userRepository.findByEmail(email)
                .isEmpty();
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
