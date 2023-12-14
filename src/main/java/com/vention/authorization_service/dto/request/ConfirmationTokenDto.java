package com.vention.authorization_service.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConfirmationTokenDto {
    private String email;
    private String token;
}
