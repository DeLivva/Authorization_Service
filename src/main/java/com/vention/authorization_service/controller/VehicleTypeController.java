package com.vention.authorization_service.controller;

import com.vention.authorization_service.domain.VehicleTypeEntity;
import com.vention.authorization_service.service.VehicleTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vehicle-type")
public class VehicleTypeController {

    private final VehicleTypeService service;

    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody VehicleTypeEntity vehicleType) {
        service.create(vehicleType);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<VehicleTypeEntity>> getAll() {
        List<VehicleTypeEntity> vehicleTypes = service.getAll();
        return ResponseEntity.ok(vehicleTypes);
    }

    @PutMapping
    public ResponseEntity<HttpStatus> update(@RequestBody VehicleTypeEntity vehicleType) {
        service.update(vehicleType);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}