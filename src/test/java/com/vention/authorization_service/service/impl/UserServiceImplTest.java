package com.vention.authorization_service.service.impl;

import com.vention.authorization_service.domain.UserEntity;
import com.vention.authorization_service.exception.DataNotFoundException;
import com.vention.authorization_service.repository.SecurityCredentialRepository;
import com.vention.authorization_service.repository.UserRepository;
import com.vention.authorization_service.service.FileService;
import com.vention.authorization_service.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityCredentialRepository securityCredentialRepository;

    @Mock
    private FileService fileService;

    private UserService userService;

    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, securityCredentialRepository, fileService);
        testUser = mock();
    }


    @Test
    void testSaveUser() {
        // given
        // when
        doReturn(testUser).when(userRepository).save(any());
        UserEntity savedUser = userService.saveUser(testUser);
        // then
        verify(userRepository, times(1)).save(any(UserEntity.class));
        assertNotNull(savedUser.getId());
        assertSame(testUser, savedUser);
    }

    @Test
    void testGetUserByEmail() {
        // given
        // when
        doReturn(Optional.of(testUser)).when(userRepository).findByEmail(any(String.class));
        UserEntity userByEmail = userService.getByEmail("test").get();
        // then
        verify(userRepository, times(1)).findByEmail(any());
        assertSame(testUser, userByEmail);
    }

    @Test
    void testGetUserByEmailWillThrow() {
        // given
        // when
        doReturn(Optional.empty()).when(userRepository).findByEmail(any());
        try {
            userService.getByEmail("test");
            // then
            fail("Expected exception, but exception is not thrown");
        } catch (DataNotFoundException e) {
            // then
            assertEquals(e.getMessage(), "Email not found");
            verify(userRepository, times(1)).findByEmail(any());
        }
    }

    @Test
    void testIsEmailUniqueTrue() {
        // given
        // when
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        boolean isUnique = userService.isEmailUnique("test");
        // then
        verify(userRepository, times(1)).findByEmail(any());
        assertTrue(isUnique);
    }

    @Test
    void testIsEmailUniqueFalse() {
        // given
        // when
        doReturn(Optional.of(testUser)).when(userRepository).findByEmail(any());
        boolean isUnique = userService.isEmailUnique("test");
        // then
        verify(userRepository, times(1)).findByEmail(any());
        assertFalse(isUnique);
    }
}