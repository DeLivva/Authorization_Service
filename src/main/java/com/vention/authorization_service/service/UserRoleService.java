package com.vention.authorization_service.service;

import com.vention.authorization_service.domain.UserRoleEntity;

public interface UserRoleService {

    UserRoleEntity getRoleByName(String roleName);
}
