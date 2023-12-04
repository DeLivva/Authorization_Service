package com.vention.authorization_service.dto.response;

import lombok.Data;

@Data
public class VehicleResponseDTO {
    private Long id;
    private String model;
    private String registrationNumber;
    private String color;
    private String type;
}
