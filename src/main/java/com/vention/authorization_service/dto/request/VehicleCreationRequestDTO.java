package com.vention.authorization_service.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VehicleCreationRequestDTO {
    @NotNull
    private Long userId;

    @NotNull
    private Long vehicleTypeId;

    @NotNull
    @Size(min = 3, max = 100)
    private String model;

    @NotNull
    @Size(min = 3, max = 100)
    private String color;

    @NotNull
    @Size(min = 4, max = 50)
    private String registrationNumber;
}
