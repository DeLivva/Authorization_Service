package com.vention.authorization_service.service.impl;

import com.vention.authorization_service.domain.UserEntity;
import com.vention.authorization_service.domain.VehicleEntity;
import com.vention.authorization_service.domain.VehicleTypeEntity;
import com.vention.authorization_service.dto.request.VehicleCreationRequestDto;
import com.vention.authorization_service.dto.request.VehicleUpdateDto;
import com.vention.authorization_service.exception.DataNotFoundException;
import com.vention.authorization_service.repository.UserRepository;
import com.vention.authorization_service.repository.VehicleRepository;
import com.vention.authorization_service.repository.VehicleTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

    @InjectMocks
    private VehicleServiceImpl vehicleService;

    @Test
    void testCreateVehicleSuccess() {
        VehicleCreationRequestDto requestDto = new VehicleCreationRequestDto();
        requestDto.setUserId(1L);
        requestDto.setVehicleTypeId(1L);
        VehicleTypeEntity vehicleType = new VehicleTypeEntity();
        UserEntity user = new UserEntity();
        when(vehicleTypeRepository.findById(anyLong())).thenReturn(Optional.of(vehicleType));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(vehicleRepository.save(any(VehicleEntity.class))).thenReturn(new VehicleEntity());

        VehicleEntity result = vehicleService.create(requestDto);

        assertNotNull(result);
        verify(vehicleRepository).save(any(VehicleEntity.class));
    }

    @Test
    void testGetByUserIdSuccess() {
        // given
        VehicleEntity vehicle = new VehicleEntity();
        when(vehicleRepository.getByUserId(anyLong())).thenReturn(Optional.of(vehicle));

        // when
        VehicleEntity result = vehicleService.getByUserId(1L);

        // then
        assertEquals(vehicle, result);
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
        VehicleEntity vehicle = new VehicleEntity();
        when(vehicleRepository.findById(anyLong())).thenReturn(Optional.of(vehicle));

        Optional<VehicleEntity> result = vehicleService.getById(1L);

        assertTrue(result.isPresent());
        assertEquals(vehicle, result.get());
    }

    @Test
    void testGetByIdNotFound() {
        when(vehicleRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<VehicleEntity> result = vehicleService.getById(1L);

        assertFalse(result.isPresent());
    }

    @Test
    void testUpdateVehicleSuccess() {
        VehicleUpdateDto updateDto = new VehicleUpdateDto();
        updateDto.setId(1L);
        updateDto.setUserId(1L);
        updateDto.setVehicleTypeId(1L);
        VehicleEntity vehicle = new VehicleEntity();
        VehicleTypeEntity vehicleType = new VehicleTypeEntity();
        when(vehicleRepository.getByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.of(vehicle));
        when(vehicleTypeRepository.findById(anyLong())).thenReturn(Optional.of(vehicleType));
        when(vehicleRepository.save(any(VehicleEntity.class))).thenReturn(vehicle);

        vehicleService.update(updateDto);

        verify(vehicleRepository).save(vehicle);
    }

    @Test
    void testUpdateVehicleNotFound() {
        VehicleUpdateDto updateDto = new VehicleUpdateDto();
        updateDto.setId(1L);
        updateDto.setUserId(1L);
        when(vehicleRepository.getByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> vehicleService.update(updateDto));
    }
}