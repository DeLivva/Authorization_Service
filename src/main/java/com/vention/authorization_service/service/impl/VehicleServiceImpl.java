package com.vention.authorization_service.service.impl;

import com.vention.authorization_service.domain.VehicleEntity;
import com.vention.authorization_service.dto.request.VehicleCreationRequestDTO;
import com.vention.authorization_service.dto.request.VehicleUpdateDTO;
import com.vention.authorization_service.dto.response.VehicleResponseDTO;
import com.vention.authorization_service.exception.DataNotFoundException;
import com.vention.authorization_service.mapper.VehicleMapper;
import com.vention.authorization_service.repository.UserRepository;
import com.vention.authorization_service.repository.VehicleRepository;
import com.vention.authorization_service.repository.VehicleTypeRepository;
import com.vention.authorization_service.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository repository;
    private final VehicleTypeRepository vehicleTypeRepository;
    private final UserRepository userRepository;
    private final VehicleMapper vehicleMapper;


    @Override
    public VehicleResponseDTO create(VehicleCreationRequestDTO requestDto) {
        var vehicleType = vehicleTypeRepository.findById(requestDto.getVehicleTypeId())
                .orElseThrow(() -> new DataNotFoundException("Vehicle type not found with id : " + requestDto.getVehicleTypeId()));
        var user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new DataNotFoundException("User not found on id : " + requestDto.getUserId()));

        var vehicle = VehicleEntity.builder()
                .color(requestDto.getColor())
                .model(requestDto.getModel())
                .user(user)
                .registrationNumber(requestDto.getRegistrationNumber())
                .vehicleType(vehicleType).build();
        return vehicleMapper.mapEntityToDto(repository.save(vehicle));
    }

    @Override
    public VehicleResponseDTO getByUserId(Long userId) {
        VehicleEntity vehicle = repository.getByUserId(userId)
                .orElseThrow(() -> new DataNotFoundException("Vehicle not found on user with id: " + userId));
        return vehicleMapper.mapEntityToDto(vehicle);
    }


    @Override
    public VehicleResponseDTO getById(Long id) {
        VehicleEntity vehicle = repository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Vehicle not found with id: " + id));
        return vehicleMapper.mapEntityToDto(vehicle);
    }

    @Override
    public void update(VehicleUpdateDTO updateDto) {
        VehicleEntity vehicle = repository.findById(updateDto.getId())
                .orElseThrow(() -> new DataNotFoundException("Vehicle not found with id: " + updateDto.getId()));

        var vehicleType = vehicleTypeRepository.findById(updateDto.getVehicleTypeId())
                .orElseThrow(() -> new DataNotFoundException("Vehicle type not found with id : " + updateDto.getVehicleTypeId()));

        vehicle.setColor(updateDto.getColor());
        vehicle.setModel(updateDto.getModel());
        vehicle.setRegistrationNumber(updateDto.getRegistrationNumber());
        vehicle.setVehicleType(vehicleType);
        repository.save(vehicle);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
