package com.vention.authorization_service.service.impl;

import com.vention.authorization_service.domain.UserEntity;
import com.vention.authorization_service.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class JwtServiceImpl implements JwtService {
    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.access.expiry}")
    private Long accessTokenExpiry;

    @Override
    public String generateAccessToken(UserEntity userEntity) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .setSubject(userEntity.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + accessTokenExpiry))
                .addClaims(Map.of("authorities", userEntity.getAuthorities()))
                .compact();
    }

    @Override
    public Jws<Claims> extractToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
    }

    @Override
    public boolean isTokenValid(String token) {
        Claims claims = extractToken(token).getBody();
        return claims.getExpiration().after(new Date());
    }
}
