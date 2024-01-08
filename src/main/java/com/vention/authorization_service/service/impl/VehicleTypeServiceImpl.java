package com.vention.authorization_service.service.impl;

import com.vention.authorization_service.domain.VehicleTypeEntity;
import com.vention.authorization_service.dto.response.VehicleTypeResponseDTO;
import com.vention.general.lib.exceptions.DataNotFoundException;
import com.vention.authorization_service.mapper.VehicleTypeMapper;
import com.vention.authorization_service.repository.VehicleTypeRepository;
import com.vention.authorization_service.service.VehicleTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehicleTypeServiceImpl implements VehicleTypeService {

    private final VehicleTypeRepository repository;
    private final VehicleTypeMapper vehicleTypeMapper;

    @Override
    public VehicleTypeResponseDTO create(VehicleTypeEntity vehicleType) {
        var vehicleTypeEntity = repository.save(vehicleType);
        return vehicleTypeMapper.convertEntityToResponseDto(vehicleTypeEntity);
    }

    @Override
    public VehicleTypeResponseDTO getById(Long id) {
        VehicleTypeEntity vehicleType = repository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Vehicle not found with id: " + id));
        return vehicleTypeMapper.convertEntityToResponseDto(vehicleType);
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
    public List<VehicleTypeResponseDTO> getAll() {
        return repository.findAll()
                .stream().map(vehicleTypeMapper::convertEntityToResponseDto)
                .collect(Collectors.toList());
    }
}
