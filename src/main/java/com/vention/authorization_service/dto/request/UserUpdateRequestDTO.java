package com.vention.authorization_service.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequestDTO {
    @NotNull
    private Long userId;

    @Pattern(regexp = "^[A-Za-z]{2,50}$")
    private String firstName;

    @Pattern(regexp = "^[A-Za-z]{2,70}$")
    private String lastName;

    @Pattern(regexp = "^[a-zA-Z]\\w{3,19}$")
    private String username;

    @Pattern(regexp = "^\\d{7,15}$")
    private String phoneNumber;
}