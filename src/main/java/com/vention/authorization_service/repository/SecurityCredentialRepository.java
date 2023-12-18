package com.vention.authorization_service.repository;

import com.vention.authorization_service.domain.SecurityCredentialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SecurityCredentialRepository extends JpaRepository<SecurityCredentialEntity, Long> {
    Optional<SecurityCredentialEntity> findByUsername(String username);

    @Query(value = """
            select s.* from security_credentials s
            inner join users u on u.credential_id = s.id
            and u.user_state not in ('DELETED')
            and u.email = :email""", nativeQuery = true)
    Optional<SecurityCredentialEntity> findByEmail(@Param("email") String email);
}
