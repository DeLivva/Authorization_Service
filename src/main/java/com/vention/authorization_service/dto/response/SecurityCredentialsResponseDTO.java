package com.vention.authorization_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecurityCredentialsResponseDTO {
    private String email;
    private List<String> roles;
}
