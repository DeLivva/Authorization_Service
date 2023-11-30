package com.vention.authorization_service.service;

import com.vention.authorization_service.domain.VehicleTypeEntity;

import java.util.List;
import java.util.Optional;

public interface VehicleTypeService {
    VehicleTypeEntity create(VehicleTypeEntity vehicleType);

    Optional<VehicleTypeEntity> getById(Long id);

    void update(VehicleTypeEntity vehicleType);

    void delete(Long id);

    List<VehicleTypeEntity> getAll();
}
