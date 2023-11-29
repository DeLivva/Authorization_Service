package com.vention.authorization_service.service;

import com.vention.authorization_service.dto.request.UserRegistrationRequestDTO;
import com.vention.authorization_service.dto.response.UserRegistrationResponseDTO;

public interface AuthenticationService {

    UserRegistrationResponseDTO registerUser(UserRegistrationRequestDTO request);

    void confirmEmail(String token);

    void sendConfirmationToken(String email);
}
