package com.vention.authorization_service.service.impl;

import com.vention.authorization_service.domain.SecurityCredentialEntity;
import com.vention.authorization_service.domain.UserEntity;
import com.vention.authorization_service.domain.UserRoleEntity;
import com.vention.authorization_service.domain.UserState;
import com.vention.authorization_service.dto.request.UserLoginRequestDto;
import com.vention.authorization_service.dto.request.UserRegistrationRequestDTO;
import com.vention.authorization_service.dto.response.JwtResponse;
import com.vention.authorization_service.dto.response.UserRegistrationResponseDTO;
import com.vention.authorization_service.exception.DataNotFoundException;
import com.vention.authorization_service.exception.DuplicateDataException;
import com.vention.authorization_service.exception.LoginFailedException;
import com.vention.authorization_service.mapper.SecurityCredentialMapper;
import com.vention.authorization_service.mapper.UserMapper;
import com.vention.authorization_service.service.JwtService;
import com.vention.authorization_service.service.MailSendingService;
import com.vention.authorization_service.service.SecurityCredentialService;
import com.vention.authorization_service.service.UserRoleService;
import com.vention.authorization_service.service.UserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import java.util.Map;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @Mock
    private JwtService jwtService;

    @Mock
    private CustomUserDetailsServiceImpl userDetailsService;

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

    @Test
    void testLoginEmailSuccess() {
        // given
        UserLoginRequestDto userLoginRequestDto = new UserLoginRequestDto("test@gmail.com", "test");
        UserEntity userEntity = new UserEntity();
        userEntity.setUserState(UserState.AUTHORIZED);
        SecurityCredentialEntity securityCredentialEntity = new SecurityCredentialEntity("test", "test", new UserRoleEntity("USER"), userEntity);
        userEntity.setCredentials(securityCredentialEntity);

        when(userService.getByEmail(userLoginRequestDto.getLogin())).thenReturn(userEntity);
        when(jwtService.generateAccessToken(any(UserEntity.class))).thenReturn("testToken");

        // when
        JwtResponse response = authenticationService.login(userLoginRequestDto);

        // then
        assertNotNull(response);
        assertEquals("testToken", response.getAccessToken());
    }

    @Test
    void testLoginEmailNotFoundFail() {
        // given
        UserLoginRequestDto userLoginRequestDto = new UserLoginRequestDto("test@gmail.com", "test");

        when(userService.getByEmail(userLoginRequestDto.getLogin())).thenThrow(DataNotFoundException.class);

        // then
        assertThrows(DataNotFoundException.class, () -> authenticationService.login(userLoginRequestDto));
    }

    @Test
    void testLoginUsernameSuccess() {
        // given
        UserLoginRequestDto userLoginRequestDto = new UserLoginRequestDto("test", "test");
        UserEntity userEntity = new UserEntity();
        userEntity.setUserState(UserState.AUTHORIZED);
        SecurityCredentialEntity securityCredentialEntity = new SecurityCredentialEntity("test", "test", new UserRoleEntity("USER"), userEntity);
        userEntity.setCredentials(securityCredentialEntity);

        when(userService.getByUsername(userLoginRequestDto.getLogin())).thenReturn(userEntity);
        when(jwtService.generateAccessToken(any(UserEntity.class))).thenReturn("testToken");

        // when
        JwtResponse response = authenticationService.login(userLoginRequestDto);

        // then
        assertNotNull(response);
        assertEquals("testToken", response.getAccessToken());
    }

    @Test
    void testLoginUsernameNotFoundFail() {
        // when
        UserLoginRequestDto userLoginRequestDto = new UserLoginRequestDto("test", "test");

        when(userService.getByUsername(userLoginRequestDto.getLogin())).thenThrow(DataNotFoundException.class);

        // then
        assertThrows(DataNotFoundException.class, () -> authenticationService.login(userLoginRequestDto));
    }

    @Test
    void testLoginAccountNotVerifiedFail() {
        // when
        UserLoginRequestDto userLoginRequestDto = new UserLoginRequestDto("test@gmail.com", "test");
        UserEntity userEntity = new UserEntity();
        userEntity.setUserState(UserState.CREATED);

        when(userService.getByEmail(userLoginRequestDto.getLogin())).thenThrow(LoginFailedException.class);

        // then
        assertThrows(LoginFailedException.class, () -> authenticationService.login(userLoginRequestDto));
    }

    @Test
    void testLoginAccountNotCompletedRegisterFail() {
        // when
        UserLoginRequestDto userLoginRequestDto = new UserLoginRequestDto("test", "test");
        UserEntity userEntity = new UserEntity();
        userEntity.setUserState(UserState.VERIFIED);
        SecurityCredentialEntity securityCredentialEntity = new SecurityCredentialEntity("test", "test", new UserRoleEntity("USER"), userEntity);
        userEntity.setCredentials(securityCredentialEntity);

        when(userService.getByUsername(userLoginRequestDto.getLogin())).thenReturn(userEntity);

        // then
        try {
            authenticationService.login(userLoginRequestDto);
        } catch (LoginFailedException e) {
            assertEquals("VERIFIED", e.getMessage());
        }
    }

    @Test
    void testLoginOAuthSuccess() {
        // given
        String userEmail = "test@gmail.com";
        UserEntity userEntity = new UserEntity();
        userEntity.setUserState(UserState.AUTHORIZED);

        when(userService.getByEmail(userEmail)).thenReturn(userEntity);
        when(jwtService.generateAccessToken(any(UserEntity.class))).thenReturn("testToken");

        // when
        JwtResponse result = authenticationService.loginOAuth(userEmail);

        // Then
        assertNotNull(result);
        assertEquals("testToken", result.getAccessToken());
    }

    @Test
    void testLoginOAuthNotFoundFail() {
        // when
        String userEmail = "test@gmail.com";

        when(userService.getByEmail(userEmail)).thenThrow(DataNotFoundException.class);
        // then
        assertThrows(DataNotFoundException.class, () -> authenticationService.loginOAuth(userEmail));
    }

    @Test
    void testLoginOAuthMissFail() {
        // when
        // then
        assertThrows(NullPointerException.class, () -> authenticationService.loginOAuth(null));
    }

    @Test
    void testAuthenticateValidTokenSuccess() {
        // Given
        String token = "validToken";
        Claims claims = mock();
        when(jwtService.extractToken(token)).thenReturn(mock());
        when(jwtService.extractToken(token).getBody()).thenReturn(claims);
        when(claims.getSubject()).thenReturn("test@gmail.com");

        UserDetails userDetails = mock();
        when(userDetailsService.loadUserByUsername("test@gmail.com")).thenReturn(userDetails);
        when(jwtService.isTokenValid(token)).thenReturn(true);

        HttpServletRequest request = mock();

        // When
        authenticationService.authenticate(token, request);

        // Then
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        verify(userDetails, times(1)).getUsername();
        verify(jwtService, times(1)).isTokenValid(token);
    }

    @Test
    void testAuthenticateExpiredTokenFail() {
        SecurityContextHolder.clearContext();
        // Given
        String token = "expiredToken";
        Claims claims = mock();
        when(jwtService.extractToken(token)).thenReturn(mock());
        when(jwtService.extractToken(token).getBody()).thenReturn(claims);
        when(claims.getSubject()).thenReturn("test@gmail.com");

        UserDetails userDetails = mock();
        when(userDetailsService.loadUserByUsername("test@gmail.com")).thenReturn(userDetails);
        when(jwtService.isTokenValid(token)).thenReturn(false);

        HttpServletRequest request = mock();

        // When
        authenticationService.authenticate(token, request);

        // Then
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}