package com.vention.authorization_service.mapper;

import com.vention.authorization_service.domain.VehicleEntity;
import com.vention.authorization_service.dto.response.VehicleResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface VehicleMapper {
    @Mapping(source = "vehicleType.name", target = "type")
    VehicleResponseDTO mapEntityToDto(VehicleEntity vehicle);
}
