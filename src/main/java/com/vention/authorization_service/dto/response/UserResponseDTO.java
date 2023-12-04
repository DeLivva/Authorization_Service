package com.vention.authorization_service.dto.response;

import lombok.Data;

@Data
public class UserResponseDTO {
    private String firstName;
    private String lastName;
    private String username;
    private String phoneNumber;
    private String email;
    public String photoLink;
}
