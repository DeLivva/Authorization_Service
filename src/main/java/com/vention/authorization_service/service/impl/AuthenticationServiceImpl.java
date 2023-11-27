package com.vention.authorization_service.service.impl;

import com.vention.authorization_service.domain.SecurityCredentialEntity;
import com.vention.authorization_service.domain.UserEntity;
import com.vention.authorization_service.domain.UserRoleEntity;
import com.vention.authorization_service.exception.DuplicateDataException;
import com.vention.authorization_service.dto.request.UserRegistrationRequest;
import com.vention.authorization_service.dto.response.GlobalResponse;
import com.vention.authorization_service.mapper.SecurityCredentialMapper;
import com.vention.authorization_service.mapper.UserMapper;
import com.vention.authorization_service.service.AuthenticationService;
import com.vention.authorization_service.service.SecurityCredentialService;
import com.vention.authorization_service.service.UserRoleService;
import com.vention.authorization_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

import static com.vention.authorization_service.utils.PropertyReader.ROLE_USER;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final SecurityCredentialService securityCredentialService;
    private final UserRoleService userRoleService;
    private final UserMapper userMapper;
    private final SecurityCredentialMapper credentialMapper;

    @Override
    @Transactional
    public GlobalResponse registerUser(UserRegistrationRequest request) {
        if(!userService.isEmailUnique(request.getEmail())) {
            throw new DuplicateDataException("This email has already been registered!!!");
        }
        UserRoleEntity userRoleEntity = userRoleService.getRoleByName(ROLE_USER);
        SecurityCredentialEntity savedCredentials = securityCredentialService.saveCredentials(
                credentialMapper.mapDataToSecurityCredentials(request.getPassword(), List.of(userRoleEntity))
        );
        UserEntity user = userMapper.mapRegistrationRequestToUserEntity(request.getEmail(), savedCredentials);
        userService.saveUser(user);
        // Request will be sent to the notification service here to confirm email
        return GlobalResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message("Please confirm your email")
                .time(ZonedDateTime.now())
                .build();
    }
}
