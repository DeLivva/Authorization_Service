package com.vention.authorization_service.repository;

import com.vention.authorization_service.domain.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<VehicleEntity, Long> {
    Optional<VehicleEntity> getByUserId(Long userId);

    Optional<VehicleEntity> getByIdAndUserId(Long id, Long userId);
    boolean existsByUserId(Long userId);
}
