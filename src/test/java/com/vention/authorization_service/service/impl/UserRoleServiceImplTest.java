package com.vention.authorization_service.service.impl;

import com.vention.authorization_service.domain.UserRoleEntity;
import com.vention.authorization_service.exception.DataNotFoundException;
import com.vention.authorization_service.repository.UserRoleRepository;
import com.vention.authorization_service.service.UserRoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
        doReturn(Optional.of(userRoleEntity)).when(userRoleRepository).findByName(any());
        UserRoleEntity roleByName = userRoleService.getByName("test");
        // then
        verify(userRoleRepository, times(1)).findByName(any());
        assertSame(roleByName, userRoleEntity);
    }

    @Test
    void testGetRoleByNameWillThrow() {
        // given
        // when
        doReturn(Optional.empty()).when(userRoleRepository).findByName(any());
        try {
            UserRoleEntity roleByName = userRoleService.getByName("test");
            fail("Expected exception but exception is not thrown");
        } catch (DataNotFoundException e) {
            verify(userRoleRepository, times(1)).findByName(any());
            assertEquals(e.getMessage(), "test role is not found");
        }
    }
}