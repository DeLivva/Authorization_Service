package com.vention.authorization_service.service.impl;

import com.vention.authorization_service.domain.ConfirmationToken;
import com.vention.authorization_service.domain.SecurityCredentialEntity;
import com.vention.authorization_service.domain.UserEntity;
import com.vention.authorization_service.domain.UserRoleEntity;
import com.vention.authorization_service.dto.request.UserRegistrationRequestDTO;
import com.vention.authorization_service.dto.response.UserRegistrationResponseDTO;
import com.vention.authorization_service.exception.DuplicateDataException;
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

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final SecurityCredentialService securityCredentialService;
    private final UserRoleService userRoleService;
    private final UserMapper userMapper;
    private final SecurityCredentialMapper credentialMapper;
    public final String DEFAULT_ROLE = "USER";
    private final MailSendingService mailSendingService;


    @Override
    @Transactional
    public UserRegistrationResponseDTO registerUser(UserRegistrationRequestDTO request) {
        if (userService.getByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateDataException("This email has already been registered!!!");
        }
        UserRoleEntity userRoleEntity = userRoleService.getByName(DEFAULT_ROLE);
        SecurityCredentialEntity savedCredentials = securityCredentialService.saveCredentials(
                credentialMapper.mapDataToSecurityCredentials(request.getPassword(), userRoleEntity)
        );
        UserEntity savedUser = userService.saveUser(
                userMapper.mapRegistrationRequestToUserEntity(request.getEmail(), savedCredentials)
        );
        // Request will be sent to the notification service here to confirm email
        return UserRegistrationResponseDTO.builder()
        mailSendingService.sendConfirmationToken(savedUser);
        return UserRegistrationResponseDTO.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .build();
    }

    @Override
    public String confirmEmail(String token) {
        ConfirmationToken confirmationToken = mailSendingService.getConfirmationToken(token);
        if (confirmationToken.getExpiredAt().after(Timestamp.valueOf(LocalDateTime.now()))) {
            confirmationToken.setConfirmedAt(Timestamp.valueOf(LocalDateTime.now()));
            UserEntity user = confirmationToken.getUser();
            user.setIsEnabled(true);
            mailSendingService.saveToken(confirmationToken);
            return "Email successfully verified";
        } else {
            return "Confirmation token expired";
        }
    }

    @Override
    public String sendConfirmationToken(String email) {
        UserEntity user = userService.getUserByEmail(email);
        mailSendingService.sendConfirmationToken(user);
        return "Confirmation link send to your email";
    }

}
