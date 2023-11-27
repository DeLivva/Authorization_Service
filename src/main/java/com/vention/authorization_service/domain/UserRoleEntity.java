package com.vention.authorization_service.domain;

import com.vention.authorization_service.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

@Entity(name = "user_roles")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleEntity extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String name;
}
