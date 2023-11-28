package com.vention.authorization_service.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserProfileFillRequestDTO {
    @NotNull
    private Long userId;

    @NotNull
    @Size(min = 3, max = 50)
    @Pattern(regexp = "^[A-Za-z]{2,50}$")
    private String firstName;

    @NotNull
    @Pattern(regexp = "^[A-Za-z]{2,70}$")
    private String lastName;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z]\\w{3,19}$")
    private String username;

    @NotNull
    @Pattern(regexp = "^\\d{7,15}$")
    private String phoneNumber;
}
