package com.vention.authorization_service.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
    private String phoneNumber;
}
