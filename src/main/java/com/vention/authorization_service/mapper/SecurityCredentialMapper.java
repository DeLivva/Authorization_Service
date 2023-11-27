package com.vention.authorization_service.mapper;

import com.vention.authorization_service.domain.SecurityCredentialEntity;
import com.vention.authorization_service.domain.UserRoleEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface SecurityCredentialMapper {

    SecurityCredentialEntity mapDataToSecurityCredentials(String password, List<UserRoleEntity> roles);
}
