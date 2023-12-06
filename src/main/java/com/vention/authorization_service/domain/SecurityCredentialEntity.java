package com.vention.authorization_service.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @OneToOne(mappedBy = "credentials")
    private UserEntity user;
}
