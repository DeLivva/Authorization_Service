package com.vention.authorization_service.service;

import com.vention.authorization_service.domain.UserEntity;

public interface UserService {

    UserEntity saveUser(UserEntity user);

    UserEntity getUserByEmail(String email);

    boolean isEmailUnique(String email);
}
