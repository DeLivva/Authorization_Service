package com.vention.authorization_service.service.impl;

import com.vention.authorization_service.domain.ConfirmationToken;
import com.vention.authorization_service.domain.SecurityCredentialEntity;
import com.vention.authorization_service.domain.UserEntity;
import com.vention.authorization_service.domain.UserRoleEntity;
import com.vention.authorization_service.dto.response.UserRegistrationResponse;
import com.vention.authorization_service.exception.ConfirmationTokenExpiredException;
import com.vention.authorization_service.exception.DuplicateDataException;
import com.vention.authorization_service.dto.request.UserRegistrationRequest;
import com.vention.authorization_service.mapper.SecurityCredentialMapper;
import com.vention.authorization_service.mapper.UserMapper;
import com.vention.authorization_service.service.AuthenticationService;
import com.vention.authorization_service.service.MailSendingService;
import com.vention.authorization_service.service.SecurityCredentialService;
import com.vention.authorization_service.service.UserRoleService;
import com.vention.authorization_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final SecurityCredentialService securityCredentialService;
    private final UserRoleService userRoleService;
    private final UserMapper userMapper;
    private final SecurityCredentialMapper credentialMapper;
    private final MailSendingService mailSendingService;


    @Override
    @Transactional
    public UserRegistrationResponse registerUser(UserRegistrationRequest request) {
        if (!userService.isEmailUnique(request.getEmail())) {
            throw new DuplicateDataException("Email " + request.getEmail() + " has already been registered!!!");
        }
        UserRoleEntity userRoleEntity = userRoleService.getRoleByName("USER");
        SecurityCredentialEntity savedCredentials = securityCredentialService.saveCredentials(
                credentialMapper.mapDataToSecurityCredentials(request.getPassword(), userRoleEntity)
        );
        UserEntity savedUser = userService.saveUser(
                userMapper.mapRegistrationRequestToUserEntity(request.getEmail(), savedCredentials)
        );
        mailSendingService.sendConfirmationToken(savedUser);
        return UserRegistrationResponse.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .build();
    }

    @Override
    public void confirmEmail(String token) {
        ConfirmationToken confirmationToken = mailSendingService.getConfirmationToken(token);
        if (confirmationToken.getExpiredAt().after(Timestamp.valueOf(LocalDateTime.now()))) {
            confirmationToken.setConfirmedAt(Timestamp.valueOf(LocalDateTime.now()));
            UserEntity user = confirmationToken.getUser();
            user.setIsEnabled(true);
            mailSendingService.saveToken(confirmationToken);
        } else {
            throw new ConfirmationTokenExpiredException("Confirmation token expired");
        }
    }

    @Override
    public void sendConfirmationToken(String email) {
        UserEntity user = userService.getUserByEmail(email);
        mailSendingService.sendConfirmationToken(user);
    }

}