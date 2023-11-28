package com.vention.authorization_service.service.impl;

import com.vention.authorization_service.domain.UserEntity;
import com.vention.authorization_service.dto.request.UserProfileFillRequestDTO;
import com.vention.authorization_service.exception.DataNotFoundException;
import com.vention.authorization_service.exception.DuplicateDataException;
import com.vention.authorization_service.repository.SecurityCredentialRepository;
import com.vention.authorization_service.repository.UserRepository;
import com.vention.authorization_service.service.FileService;
import com.vention.authorization_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final SecurityCredentialRepository securityCredentialRepository;
    private final FileService fileService;

    @Override
    public UserEntity saveUser(UserEntity user) {
        return repository.save(user);
    }

    @Override
    public Optional<UserEntity> getByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public boolean isEmailUnique(String email) {
        return repository.findByEmail(email)
                .isEmpty();
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
        credentials.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        securityCredentialRepository.save(credentials);

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        repository.save(user);
        return user;
    }

    @Override
    public String uploadProfilePicture(Long userId, MultipartFile file) {
        String savedLocation = fileService.uploadFile(file);
        var user = repository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found"));
        user.setPhoto(savedLocation);
        user.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        repository.save(user);
        return savedLocation;
    }
}
