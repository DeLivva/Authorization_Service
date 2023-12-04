package com.vention.authorization_service.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VehicleUpdateDTO extends VehicleCreationRequestDTO {
    @NotNull
    private Long id;
}