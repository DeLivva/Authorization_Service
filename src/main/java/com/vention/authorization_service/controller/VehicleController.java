package com.vention.authorization_service.controller;

import com.vention.authorization_service.dto.request.VehicleCreationRequestDTO;
import com.vention.authorization_service.dto.request.VehicleUpdateDTO;
import com.vention.authorization_service.dto.response.VehicleResponseDTO;
import com.vention.authorization_service.service.VehicleService;
import jakarta.validation.Valid;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/vehicles")
public class VehicleController {
    private final VehicleService service;

    @PostMapping
    public ResponseEntity<VehicleResponseDTO> create(@RequestBody @Valid VehicleCreationRequestDTO requestDto) {
        VehicleResponseDTO vehicle = service.create(requestDto);
        return new ResponseEntity<>(vehicle, HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<VehicleResponseDTO> getByUserId(@RequestParam Long userId) {
        VehicleResponseDTO vehicle = service.getByUserId(userId);
        return ResponseEntity.ok(vehicle);
    }

    @PutMapping
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid VehicleUpdateDTO vehicleUpdateDto) {
        service.update(vehicleUpdateDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}