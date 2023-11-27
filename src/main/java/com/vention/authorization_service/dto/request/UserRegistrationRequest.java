package com.vention.authorization_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationRequest {

    @Pattern(regexp = "^[A-Za-z][A-Za-z0-9+_.-]*@[A-Za-z0-9.-]+$")
    private String email;

    @Pattern(regexp = ".{8,16}")
    private String password;
}
