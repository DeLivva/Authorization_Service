package com.vention.authorization_service.repository;

import com.vention.authorization_service.domain.SecurityCredentialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecurityCredentialRepository extends JpaRepository<SecurityCredentialEntity, Long> {

}
