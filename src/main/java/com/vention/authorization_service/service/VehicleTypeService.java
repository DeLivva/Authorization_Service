package com.vention.authorization_service.service;

import com.vention.authorization_service.domain.VehicleTypeEntity;
import com.vention.authorization_service.dto.response.VehicleTypeResponseDTO;

import java.util.List;

public interface VehicleTypeService {
    VehicleTypeResponseDTO create(VehicleTypeEntity vehicleType);

    VehicleTypeResponseDTO getById(Long id);

    void update(VehicleTypeEntity vehicleType);

    void delete(Long id);

    List<VehicleTypeResponseDTO> getAll();
}
