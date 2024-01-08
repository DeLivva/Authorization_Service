package com.vention.authorization_service.service.impl;

import com.vention.authorization_service.domain.UserEntity;
import com.vention.authorization_service.domain.VehicleEntity;
import com.vention.authorization_service.domain.VehicleTypeEntity;
import com.vention.authorization_service.dto.request.VehicleCreationRequestDTO;
import com.vention.authorization_service.dto.request.VehicleUpdateDTO;
import com.vention.authorization_service.dto.response.VehicleResponseDTO;
import com.vention.authorization_service.mapper.VehicleMapper;
import com.vention.authorization_service.repository.UserRepository;
import com.vention.authorization_service.repository.VehicleRepository;
import com.vention.authorization_service.repository.VehicleTypeRepository;
import com.vention.general.lib.exceptions.DataNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VehicleServiceImplTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private VehicleTypeRepository vehicleTypeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private VehicleMapper vehicleMapper;

    @InjectMocks
    private VehicleServiceImpl vehicleService;

    @Test
    void testCreateVehicleSuccess() {
        VehicleCreationRequestDTO requestDto = new VehicleCreationRequestDTO();
        requestDto.setUserId(1L);
        requestDto.setVehicleTypeId(1L);
        VehicleTypeEntity vehicleType = new VehicleTypeEntity();
        UserEntity user = new UserEntity();
        when(vehicleTypeRepository.findById(anyLong())).thenReturn(Optional.of(vehicleType));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(vehicleRepository.save(any(VehicleEntity.class))).thenReturn(new VehicleEntity());
        when(vehicleMapper.mapEntityToDto(any(VehicleEntity.class))).thenReturn(new VehicleResponseDTO());

        VehicleResponseDTO result = vehicleService.create(requestDto);

        assertNotNull(result);
        verify(vehicleRepository).save(any(VehicleEntity.class));
    }

    @Test
    void testGetByUserIdSuccess() {
        Long userId = 1L;
        VehicleEntity mockVehicle = new VehicleEntity();
        when(vehicleRepository.getByUserId(userId)).thenReturn(Optional.of(mockVehicle));
        when(vehicleMapper.mapEntityToDto(mockVehicle)).thenReturn(new VehicleResponseDTO());

        VehicleResponseDTO result = vehicleService.getByUserId(userId);

        assertNotNull(result);
    }

    @Test
    void testGetByUserIdNotFound() {
        // when
        when(vehicleRepository.getByUserId(anyLong())).thenReturn(Optional.empty());

        // then
        assertThrows(DataNotFoundException.class, () -> vehicleService.getByUserId(1L));
    }


    @Test
    void testGetByIdFound() {
        Long id = 1L;
        VehicleEntity mockVehicle = new VehicleEntity();
        when(vehicleRepository.findById(id)).thenReturn(Optional.of(mockVehicle));
        when(vehicleMapper.mapEntityToDto(mockVehicle)).thenReturn(new VehicleResponseDTO());

        VehicleResponseDTO result = vehicleService.getById(id);

        assertNotNull(result);
    }

    @Test
    void testGetByIdNotFound() {
        when(vehicleRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> vehicleService.getById(1L));
    }

    @Test
    void testUpdateVehicleSuccess() {
        VehicleUpdateDTO updateDto = new VehicleUpdateDTO();
        updateDto.setId(1L);
        updateDto.setUserId(1L);
        updateDto.setVehicleTypeId(1L);
        VehicleEntity vehicle = new VehicleEntity();
        VehicleTypeEntity vehicleType = new VehicleTypeEntity();
        when(vehicleRepository.findById(anyLong())).thenReturn(Optional.of(vehicle));
        when(vehicleTypeRepository.findById(anyLong())).thenReturn(Optional.of(vehicleType));
        when(vehicleRepository.save(any(VehicleEntity.class))).thenReturn(vehicle);

        vehicleService.update(updateDto);

        verify(vehicleRepository).save(vehicle);
    }

    @Test
    void testUpdateVehicleNotFound() {
        VehicleUpdateDTO updateDto = new VehicleUpdateDTO();
        updateDto.setId(1L);
        updateDto.setUserId(1L);
        when(vehicleRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> vehicleService.update(updateDto));
    }
}