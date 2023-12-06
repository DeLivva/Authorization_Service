package com.vention.authorization_service.mapper;

import com.vention.authorization_service.domain.SecurityCredentialEntity;
import com.vention.authorization_service.domain.UserEntity;
import com.vention.authorization_service.dto.response.UserResponseDTO;
import com.vention.authorization_service.dto.response.UserUpdateResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserMapper {

    UserEntity mapRegistrationRequestToUserEntity(String email, SecurityCredentialEntity credentials);

    @Mapping(source = "credentials.username", target = "username")
    UserUpdateResponseDTO mapUserEntityToUpdateResponseDto(UserEntity user);

    @Mapping(source = "credentials.username", target = "username")
    @Mapping(source = "photo", target = "photoLink")
    UserResponseDTO mapEntityToResponseDto(UserEntity user);
}
