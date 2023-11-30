package com.vention.authorization_service.mapper;

import com.vention.authorization_service.domain.SecurityCredentialEntity;
import com.vention.authorization_service.domain.UserEntity;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

    UserEntity mapRegistrationRequestToUserEntity(String email, SecurityCredentialEntity credentials);
}
