package com.vention.authorization_service.service;

import com.vention.authorization_service.dto.request.UserLoginRequestDto;
import com.vention.authorization_service.dto.request.UserRegistrationRequestDTO;
import com.vention.authorization_service.dto.response.JwtResponse;
import com.vention.authorization_service.dto.response.UserRegistrationResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

public interface AuthenticationService {

    UserRegistrationResponseDTO registerUser(UserRegistrationRequestDTO request);

    void confirmEmail(String token);

    void sendConfirmationToken(String email);

    JwtResponse loginOAuth(OAuth2AuthenticationToken token);

    JwtResponse login(UserLoginRequestDto userLoginRequestDto);

    void authenticate(String token, HttpServletRequest request);
}
