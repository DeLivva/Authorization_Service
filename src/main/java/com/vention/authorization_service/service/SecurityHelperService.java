package com.vention.authorization_service.service;

import com.vention.authorization_service.dto.response.UserResponseDTO;

public interface SecurityHelperService {
    UserResponseDTO getCurrentUser();
}
