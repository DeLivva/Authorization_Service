package com.vention.authorization_service.mapper;

import com.vention.authorization_service.domain.VehicleTypeEntity;
import com.vention.authorization_service.dto.response.VehicleTypeResponseDTO;
import org.mapstruct.Mapper;

@Mapper
public interface VehicleTypeMapper {
    VehicleTypeResponseDTO convertEntityToResponseDto(VehicleTypeEntity vehicleType);
}
