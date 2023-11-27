package com.vention.authorization_service.service.impl;

import com.vention.authorization_service.domain.UserRoleEntity;
import com.vention.authorization_service.exception.DataNotFoundException;
import com.vention.authorization_service.repository.UserRoleRepository;
import com.vention.authorization_service.service.UserRoleService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRoleServiceImplTest {

    @Mock
    private UserRoleRepository userRoleRepository;

    private UserRoleService userRoleService;

    private UserRoleEntity userRoleEntity;

    @BeforeEach
    void setUp() {
        userRoleService = new UserRoleServiceImpl(userRoleRepository);
        userRoleEntity = mock();
    }

    @Test
    void testGetRoleByName() {
        // given
        // when
        doReturn(Optional.of(userRoleEntity)).when(userRoleRepository).findUserRoleEntityByName(any());
        UserRoleEntity roleByName = userRoleService.getRoleByName("test");
        // then
        verify(userRoleRepository, times(1)).findUserRoleEntityByName(any());
        assertSame(roleByName, userRoleEntity);
    }

    @Test
    void testGetRoleByNameWillThrow() {
        // given
        // when
        doReturn(Optional.empty()).when(userRoleRepository).findUserRoleEntityByName(any());
        try {
            UserRoleEntity roleByName = userRoleService.getRoleByName("test");
            fail("Expected exception but exception is not thrown");
        } catch (DataNotFoundException e) {
            verify(userRoleRepository, times(1)).findUserRoleEntityByName(any());
            assertEquals(e.getMessage(), "test role is not found");
        }
    }
}