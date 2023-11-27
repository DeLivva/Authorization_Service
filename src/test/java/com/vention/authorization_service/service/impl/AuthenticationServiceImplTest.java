package com.vention.authorization_service.service.impl;

import com.vention.authorization_service.domain.SecurityCredentialEntity;
import com.vention.authorization_service.domain.UserEntity;
import com.vention.authorization_service.domain.UserRoleEntity;
import com.vention.authorization_service.dto.request.UserRegistrationRequest;
import com.vention.authorization_service.dto.response.GlobalResponse;
import com.vention.authorization_service.exception.DuplicateDataException;
import com.vention.authorization_service.mapper.SecurityCredentialMapper;
import com.vention.authorization_service.mapper.UserMapper;
import com.vention.authorization_service.service.AuthenticationService;
import com.vention.authorization_service.service.SecurityCredentialService;
import com.vention.authorization_service.service.UserRoleService;
import com.vention.authorization_service.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private SecurityCredentialService securityCredentialService;

    @Mock
    private UserRoleService userRoleService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private SecurityCredentialMapper credentialMapper;

    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        authenticationService = new AuthenticationServiceImpl(
                userService, securityCredentialService, userRoleService, userMapper, credentialMapper
        );
    }

    @Test
    void testRegisterUser() {
        // given
        UserRoleEntity userRoleEntity = mock();
        SecurityCredentialEntity credentialEntity = mock();
        UserEntity userEntity = mock();
        UserRegistrationRequest request = mock();
        // when
        doReturn(true).when(userService).isEmailUnique(any());
        doReturn(userRoleEntity).when(userRoleService).getRoleByName(any());
        doReturn(credentialEntity).when(securityCredentialService).saveCredentials(any());
        doReturn(userEntity).when(userService).saveUser(any());
        GlobalResponse globalResponse = authenticationService.registerUser(request);
        // then
        verify(userService, times(1)).isEmailUnique(any());
        verify(userRoleService, times(1)).getRoleByName(any());
        verify(securityCredentialService, times(1)).saveCredentials(any());
        verify(userService, times(1)).saveUser(any());
        assertEquals(globalResponse.getStatus(), 201);
    }

    @Test
    void testRegisterUserWillThrow() {
        // given
        UserRegistrationRequest request = mock();
        // when
        doReturn(false).when(userService).isEmailUnique(any());
        try {
            GlobalResponse globalResponse = authenticationService.registerUser(request);
            fail("Expected exception but exception is not thrown");
        } catch (DuplicateDataException e) {
            assertEquals(e.getMessage(), "This email has already been registered!!!");
        }
        verify(userService, times(1)).isEmailUnique(any());
        verify(userRoleService, never()).getRoleByName(any());
        verify(securityCredentialService, never()).saveCredentials(any());
        verify(userService, never()).saveUser(any());
    }
}