package com.vention.authorization_service.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.*;

import java.util.List;

@Entity(name = "security_credentials")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SecurityCredentialEntity extends BaseEntity {

    private String username;

    private String password;

    @ManyToMany
    @JoinTable(
            name = "credential_roles",
            joinColumns = @JoinColumn(name = "user_credential_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<UserRoleEntity> roles;
}
