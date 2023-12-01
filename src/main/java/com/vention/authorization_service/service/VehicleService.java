package com.vention.authorization_service.service;

import com.vention.authorization_service.dto.request.VehicleCreationRequestDTO;
import com.vention.authorization_service.dto.request.VehicleUpdateDTO;
import com.vention.authorization_service.dto.response.VehicleResponseDTO;

public interface VehicleService {

    VehicleResponseDTO create(VehicleCreationRequestDTO requestDto);

    VehicleResponseDTO getById(Long id);

    void delete(Long id);

    VehicleResponseDTO getByUserId(Long userId);

    void update(VehicleUpdateDTO updateDto);
}
