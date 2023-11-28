package com.vention.authorization_service.service.impl;

import com.vention.authorization_service.domain.SecurityCredentialEntity;
import com.vention.authorization_service.domain.UserEntity;
import com.vention.authorization_service.domain.UserRoleEntity;
import com.vention.authorization_service.dto.response.UserRegistrationResponse;
import com.vention.authorization_service.exception.DuplicateDataException;
import com.vention.authorization_service.dto.request.UserRegistrationRequest;
import com.vention.authorization_service.mapper.SecurityCredentialMapper;
import com.vention.authorization_service.mapper.UserMapper;
import com.vention.authorization_service.service.AuthenticationService;
import com.vention.authorization_service.service.SecurityCredentialService;
import com.vention.authorization_service.service.UserRoleService;
import com.vention.authorization_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public UserRegistrationResponse registerUser(UserRegistrationRequest request) {
        if(!userService.isEmailUnique(request.getEmail())) {
            throw new DuplicateDataException("This email has already been registered!!!");
        }
        UserRoleEntity userRoleEntity = userRoleService.getRoleByName(ROLE_USER);
        SecurityCredentialEntity savedCredentials = securityCredentialService.saveCredentials(
                credentialMapper.mapDataToSecurityCredentials(request.getPassword(), List.of(userRoleEntity))
        );
        UserEntity savedUser = userService.saveUser(
                userMapper.mapRegistrationRequestToUserEntity(request.getEmail(), savedCredentials)
        );
        // Request will be sent to the notification service here to confirm email
        return UserRegistrationResponse.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .build();
    }
}
