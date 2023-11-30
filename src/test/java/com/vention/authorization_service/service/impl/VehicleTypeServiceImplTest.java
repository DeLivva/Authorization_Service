package com.vention.authorization_service.service.impl;

import com.vention.authorization_service.domain.VehicleTypeEntity;
import com.vention.authorization_service.exception.DataNotFoundException;
import com.vention.authorization_service.repository.VehicleTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VehicleTypeServiceImplTest {

    @Mock
    private VehicleTypeRepository repository;

    @InjectMocks
    private VehicleTypeServiceImpl service;

    @Test
    void createVehicleType() {
        VehicleTypeEntity vehicleType = new VehicleTypeEntity();
        when(repository.save(any(VehicleTypeEntity.class))).thenReturn(vehicleType);

        VehicleTypeEntity created = service.create(new VehicleTypeEntity());

        assertNotNull(created);
        verify(repository).save(any(VehicleTypeEntity.class));
    }

    @Test
    void getVehicleTypeByIdFound() {
        Long id = 1L;
        Optional<VehicleTypeEntity> vehicleType = Optional.of(new VehicleTypeEntity());
        when(repository.findById(id)).thenReturn(vehicleType);

        Optional<VehicleTypeEntity> found = service.getById(id);

        assertTrue(found.isPresent());
        verify(repository).findById(id);
    }

    @Test
    void getVehicleTypeByIdNotFound() {
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        Optional<VehicleTypeEntity> found = service.getById(id);

        assertFalse(found.isPresent());
        verify(repository).findById(id);
    }

    @Test
    void updateExistingVehicleType() {
        VehicleTypeEntity vehicleType = new VehicleTypeEntity();
        vehicleType.setId(1L);
        when(repository.findById(vehicleType.getId())).thenReturn(Optional.of(vehicleType));

        assertDoesNotThrow(() -> service.update(vehicleType));
        verify(repository).save(vehicleType);
    }

    @Test
    void updateNonExistingVehicleType() {
        VehicleTypeEntity vehicleType = new VehicleTypeEntity();
        vehicleType.setId(1L);
        when(repository.findById(vehicleType.getId())).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> service.update(vehicleType));
    }

    @Test
    void deleteVehicleType() {
        Long id = 1L;
        doNothing().when(repository).deleteById(id);

        service.delete(id);

        verify(repository).deleteById(id);
    }

    @Test
    void getAllVehicleTypes() {
        List<VehicleTypeEntity> list = new ArrayList<>();
        list.add(new VehicleTypeEntity());
        when(repository.findAll()).thenReturn(list);

        List<VehicleTypeEntity> result = service.getAll();

        assertFalse(result.isEmpty());
        verify(repository).findAll();
    }
}