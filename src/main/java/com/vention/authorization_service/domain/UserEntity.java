package com.vention.authorization_service.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.*;

@Entity(name = "users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String email;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    @Builder.Default
    private Boolean isEnabled = false;

    @OneToOne
    @JoinColumn(name = "credential_id", referencedColumnName = "id")
    private SecurityCredentialEntity credentials;
}
