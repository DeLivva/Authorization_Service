package com.vention.authorization_service.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VehicleUpdateDto extends VehicleCreationRequestDto {
    @NotNull
    private Long id;
}