package com.vention.authorization_service.service.impl;

import com.vention.authorization_service.domain.VehicleTypeEntity;
import com.vention.authorization_service.exception.DataNotFoundException;
import com.vention.authorization_service.repository.VehicleTypeRepository;
import com.vention.authorization_service.service.VehicleTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VehicleTypeServiceImpl implements VehicleTypeService {

    private final VehicleTypeRepository repository;

    @Override
    public VehicleTypeEntity create(VehicleTypeEntity vehicleType) {
        return repository.save(vehicleType);
    }

    @Override
    public Optional<VehicleTypeEntity> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void update(VehicleTypeEntity vehicleType) {
        if (repository.findById(vehicleType.getId()).isEmpty()) {
            throw new DataNotFoundException("Vehicle type not found with id: " + vehicleType.getId());
        }
        repository.save(vehicleType);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<VehicleTypeEntity> getAll() {
        return repository.findAll();
    }
}
