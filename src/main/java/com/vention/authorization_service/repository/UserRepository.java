package com.vention.authorization_service.repository;

import com.vention.authorization_service.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query(value = "select u.* from users u where u.email = :email and u.user_state != 'DELETED'", nativeQuery = true)
    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByCredentials_Username(String username);

    @Query(value = """
            select u.* from users u inner join vehicle v on v.user_id = u.id
            inner join vehicle_type t on t.id = v.type_id and t.name = :car_type where user_state <> 'DELETED'""", nativeQuery = true)
    List<UserEntity> getByCarType(@Param("car_type") String carType);

    @Query(value = """
            select u.* from users u inner join vehicle v on v.user_id = u.id
            inner join vehicle_type t on t.id = v.type_id""", nativeQuery = true)
    List<UserEntity> getAllCouriers();
}
