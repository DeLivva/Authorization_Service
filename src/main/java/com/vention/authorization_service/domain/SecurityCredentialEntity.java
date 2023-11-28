package com.vention.authorization_service.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "security_credentials")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SecurityCredentialEntity extends BaseEntity {

    private String username;

    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private UserRoleEntity role;
}
