package com.vention.authorization_service.service;

import com.vention.authorization_service.dto.request.UserRegistrationRequest;
import com.vention.authorization_service.dto.response.UserRegistrationResponse;

public interface AuthenticationService {
    UserRegistrationResponse registerUser(UserRegistrationRequest request);

    void confirmEmail(String token);

    void sendConfirmationToken(String email);
}
