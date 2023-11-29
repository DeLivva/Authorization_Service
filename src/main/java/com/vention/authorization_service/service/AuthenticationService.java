package com.vention.authorization_service.service;

import com.vention.authorization_service.dto.request.UserRegistrationRequestDTO;
import com.vention.authorization_service.dto.response.UserRegistrationResponseDTO;

public interface AuthenticationService {

    UserRegistrationResponseDTO registerUser(UserRegistrationRequestDTO request);

    String confirmEmail(String token);

    String sendConfirmationToken(String email);
}
