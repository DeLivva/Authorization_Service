package com.vention.authorization_service.service;

import com.vention.authorization_service.domain.VehicleEntity;
import com.vention.authorization_service.dto.request.VehicleCreationRequestDTO;
import com.vention.authorization_service.dto.request.VehicleUpdateDTO;

import java.util.Optional;

public interface VehicleService {

    VehicleEntity create(VehicleCreationRequestDTO requestDto);

    Optional<VehicleEntity> getById(Long id);

    void delete(Long id);

    VehicleEntity getByUserId(Long userId);

    void update(VehicleUpdateDTO updateDto);
}
