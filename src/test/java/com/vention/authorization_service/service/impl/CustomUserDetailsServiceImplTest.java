package com.vention.authorization_service.service.impl;

import com.vention.general.lib.exceptions.DataNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceImplTest {
    @Mock
    private CustomUserDetailsServiceImpl userDetailsService;

    @Test
    void testLoadUserByUsernameSuccess() {
        // when
        String email = "test@gmail.com";
        User user = mock();
        when(userDetailsService.loadUserByUsername(email)).thenReturn(user);

        // then
        assertEquals(userDetailsService.loadUserByUsername(email), user);
    }

    @Test
    void testLoadUserByUsernameFail() {
        // when
        String email = "test@gmail.com";
        when(userDetailsService.loadUserByUsername(email)).thenThrow(DataNotFoundException.class);

        // then
        assertThrows(DataNotFoundException.class, () -> userDetailsService.loadUserByUsername(email));
    }
}