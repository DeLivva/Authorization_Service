package com.vention.authorization_service.dto.response;

import lombok.Data;

@Data
public class UserLoginResponseDto extends UserResponseDTO{
    private String role;
    private Boolean isEnabled;
}
