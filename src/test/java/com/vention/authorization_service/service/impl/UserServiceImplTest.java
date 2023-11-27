package com.vention.authorization_service.service.impl;

import com.vention.authorization_service.domain.UserEntity;
import com.vention.authorization_service.exception.DataNotFoundException;
import com.vention.authorization_service.exception.DuplicateDataException;
import com.vention.authorization_service.repository.UserRepository;
import com.vention.authorization_service.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository);
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
        doReturn(Optional.of(testUser)).when(userRepository).findUserEntityByEmail(any(String.class));
        UserEntity userByEmail = userService.getUserByEmail("test");
        // then
        verify(userRepository, times(1)).findUserEntityByEmail(any());
        assertSame(testUser, userByEmail);
    }

    @Test
    void testGetUserByEmailWillThrow() {
        // given
        // when
        doReturn(Optional.empty()).when(userRepository).findUserEntityByEmail(any());
        try {
            userService.getUserByEmail("test");
            // then
            fail("Expected exception, but exception is not thrown");
        } catch (DataNotFoundException e) {
            // then
            assertEquals(e.getMessage(), "Email not found");
            verify(userRepository, times(1)).findUserEntityByEmail(any());
        }
    }

    @Test
    void testIsEmailUniqueTrue() {
        // given
        // when
        when(userRepository.findUserEntityByEmail(any())).thenReturn(Optional.empty());
        boolean isUnique = userService.isEmailUnique("test");
        // then
        verify(userRepository, times(1)).findUserEntityByEmail(any());
        assertTrue(isUnique);
    }

    @Test
    void testIsEmailUniqueFalse() {
        // given
        // when
        doReturn(Optional.of(testUser)).when(userRepository).findUserEntityByEmail(any());
        boolean isUnique = userService.isEmailUnique("test");
        // then
        verify(userRepository, times(1)).findUserEntityByEmail(any());
        assertFalse(isUnique);
    }
}