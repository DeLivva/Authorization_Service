package com.vention.authorization_service.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private String photo;

    @Builder.Default
    private Boolean isEnabled = false;

    @Column(name = "is_deleted", columnDefinition = "boolean default false")
    private Boolean isDeleted;

    @OneToOne
    @JoinColumn(name = "credential_id", referencedColumnName = "id")
    private SecurityCredentialEntity credentials;
}
