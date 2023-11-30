package com.vention.authorization_service.service.impl;

import com.vention.authorization_service.domain.VehicleEntity;
import com.vention.authorization_service.dto.request.VehicleCreationRequestDto;
import com.vention.authorization_service.dto.request.VehicleUpdateDto;
import com.vention.authorization_service.exception.DataNotFoundException;
import com.vention.authorization_service.repository.UserRepository;
import com.vention.authorization_service.repository.VehicleRepository;
import com.vention.authorization_service.repository.VehicleTypeRepository;
import com.vention.authorization_service.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository repository;
    private final VehicleTypeRepository vehicleTypeRepository;
    private final UserRepository userRepository;


    @Override
    public VehicleEntity create(VehicleCreationRequestDto requestDto) {
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
        return repository.save(vehicle);
    }

    @Override
    public VehicleEntity getByUserId(Long userId) {
        return repository.getByUserId(userId)
                .orElseThrow(() -> new DataNotFoundException("Vehicle not found on user with id: " + userId));
    }


    @Override
    public Optional<VehicleEntity> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void update(VehicleUpdateDto updateDto) {
        VehicleEntity vehicle = repository.getByIdAndUserId(updateDto.getId(), updateDto.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Vehicle not found with id: " + updateDto.getId() + " and userId: " + updateDto.getUserId()));

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
