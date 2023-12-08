package com.vention.authorization_service.service.impl;

import com.vention.authorization_service.domain.UserEntity;
import com.vention.authorization_service.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtServiceImplTest {
    @Mock
    private JwtService jwtService;

    @Test
    void testGenerateAccessTokenSuccess() {
        // when
        String accessToken = "testToken";
        UserEntity userEntity = mock();
        when(jwtService.generateAccessToken(userEntity)).thenReturn(accessToken);

        // then
        assertSame(accessToken, jwtService.generateAccessToken(userEntity));
    }

    @Test
    void testExtractTokenSuccess() {
        String token = "testToken";
        Jws<Claims> claims = mock();

        when(jwtService.extractToken(token)).thenReturn(claims);

        assertEquals(claims, jwtService.extractToken(token));
    }

}