package com.vention.authorization_service.service;

import com.vention.authorization_service.domain.VehicleEntity;
import com.vention.authorization_service.dto.request.VehicleCreationRequestDto;
import com.vention.authorization_service.dto.request.VehicleUpdateDto;

import java.util.Optional;

public interface VehicleService {

    VehicleEntity create(VehicleCreationRequestDto requestDto);

    Optional<VehicleEntity> getById(Long id);

    void delete(Long id);

    VehicleEntity getByUserId(Long userId);

    void update(VehicleUpdateDto updateDto);
}
