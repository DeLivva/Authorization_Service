package com.vention.authorization_service.service.impl;

import com.vention.authorization_service.domain.SecurityCredentialEntity;
import com.vention.authorization_service.domain.UserEntity;
import com.vention.authorization_service.domain.UserRoleEntity;
import com.vention.authorization_service.dto.request.UserRegistrationRequestDTO;
import com.vention.authorization_service.dto.response.UserRegistrationResponseDTO;
import com.vention.authorization_service.exception.DuplicateDataException;
import com.vention.authorization_service.mapper.SecurityCredentialMapper;
import com.vention.authorization_service.mapper.UserMapper;
import com.vention.authorization_service.service.MailSendingService;
import com.vention.authorization_service.service.SecurityCredentialService;
import com.vention.authorization_service.service.UserRoleService;
import com.vention.authorization_service.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

    @Mock
    private MailSendingService mailSendingService;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Test
    void testRegisterUser() {
        // given
        UserRoleEntity userRoleEntity = mock();
        SecurityCredentialEntity credentialEntity = mock();
        UserEntity userEntity = mock();
        UserRegistrationRequestDTO request = mock();
        // when
        doReturn(true).when(userService).isEligibleForRegistration(any());
        doReturn(userRoleEntity).when(userRoleService).getByName(any());
        doReturn(credentialEntity).when(securityCredentialService).saveCredentials(any());
        doReturn(userEntity).when(userService).saveUser(any());
        UserRegistrationResponseDTO response = authenticationService.registerUser(request);
        // then
        verify(userService, times(1)).isEligibleForRegistration(any());
        verify(userRoleService, times(1)).getByName(any());
        verify(securityCredentialService, times(1)).saveCredentials(any());
        verify(userService, times(1)).saveUser(any());
        assertNotNull(response.getId());
    }

    @Test
    void testRegisterUserWillThrow() {
        // given
        UserRegistrationRequestDTO request = mock();
        // when
        doReturn(false).when(userService).isEligibleForRegistration(any());
        try {
            authenticationService.registerUser(request);
            fail("Expected exception but exception is not thrown");
        } catch (DuplicateDataException e) {
            assertEquals(e.getMessage(), "This email has already been registered!!!");
        }
        verify(userService, times(1)).isEligibleForRegistration(any());
        verify(userRoleService, never()).getByName(any());
        verify(securityCredentialService, never()).saveCredentials(any());
        verify(userService, never()).saveUser(any());
    }
}