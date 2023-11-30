package com.vention.authorization_service.service.impl;

import com.vention.authorization_service.domain.UserEntity;
import com.vention.authorization_service.dto.request.UserProfileFillRequestDTO;
import com.vention.authorization_service.exception.DataNotFoundException;
import com.vention.authorization_service.exception.DuplicateDataException;
import com.vention.authorization_service.exception.InvalidFileTypeException;
import com.vention.authorization_service.repository.SecurityCredentialRepository;
import com.vention.authorization_service.repository.UserRepository;
import com.vention.authorization_service.service.FileService;
import com.vention.authorization_service.service.UserService;
import com.vention.authorization_service.utils.FileUtils;
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
}
