package com.vention.authorization_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateResponseDTO {
    private String firstName;
    private String lastName;
    private String username;
    private String phoneNumber;
    private String email;
}
