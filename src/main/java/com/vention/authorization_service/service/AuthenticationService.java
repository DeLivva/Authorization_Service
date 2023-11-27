package com.vention.authorization_service.service;

import com.vention.authorization_service.dto.request.UserRegistrationRequest;
import com.vention.authorization_service.dto.response.GlobalResponse;

public interface AuthenticationService {

    GlobalResponse registerUser(UserRegistrationRequest request);
}
