package com.vention.authorization_service.service.impl;

import com.vention.authorization_service.domain.UserRoleEntity;
import com.vention.authorization_service.exception.DataNotFoundException;
import com.vention.authorization_service.repository.UserRoleRepository;
import com.vention.authorization_service.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository userRoleRepository;

    @Override
    public UserRoleEntity getRoleByName(String roleName) {
        return userRoleRepository.findUserRoleEntityByName(roleName).orElseThrow(
                () -> new DataNotFoundException(roleName + " role is not found")
        );
    }
}
