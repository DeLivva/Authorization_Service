package com.vention.authorization_service.repository;

import com.vention.authorization_service.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query(value = "select u from users u where u.email = :email and u.userState != 'DELETED'")
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByCredentials_Username(String username);
}
