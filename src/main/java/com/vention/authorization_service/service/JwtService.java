package com.vention.authorization_service.service;

import com.vention.authorization_service.domain.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

public interface JwtService {
     String generateAccessToken(UserEntity userEntity);
     Jws<Claims> extractToken(String token);
     boolean isTokenValid(String token);
}
