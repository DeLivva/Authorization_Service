package com.vention.authorization_service.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vehicle")
public class VehicleEntity extends BaseEntity {

    @Column(name = "model")
    private String model;

    @Column(name = "registration_number")
    private String registrationNumber;

    @Column(name = "color")
    private String color;

    @OneToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_vehicle_user"))
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "type_id", foreignKey = @ForeignKey(name = "fk_vehicle_car_type"))
    private VehicleTypeEntity vehicleType;
}
