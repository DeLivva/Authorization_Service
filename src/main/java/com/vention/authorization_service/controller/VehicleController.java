package com.vention.authorization_service.controller;

import com.vention.authorization_service.domain.VehicleEntity;
import com.vention.authorization_service.dto.request.VehicleCreationRequestDto;
import com.vention.authorization_service.dto.request.VehicleUpdateDto;
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
@RequestMapping("/vehicle")
public class VehicleController {
    private final VehicleService service;

    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid VehicleCreationRequestDto requestDto) {
        service.create(requestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<VehicleEntity> getByUserId(@RequestParam Long userId) {
        VehicleEntity vehicle = service.getByUserId(userId);
        return ResponseEntity.ok(vehicle);
    }

    @PutMapping
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid VehicleUpdateDto vehicleUpdateDto) {
        service.update(vehicleUpdateDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}