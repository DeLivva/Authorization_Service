package com.vention.authorization_service.service.impl;

import com.vention.authorization_service.domain.UserRoleEntity;
import com.vention.general.lib.exceptions.DataNotFoundException;
import com.vention.authorization_service.repository.UserRoleRepository;
import com.vention.authorization_service.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository userRoleRepository;

    @Override
    public UserRoleEntity getByName(String roleName) {
        return userRoleRepository.findByName(roleName).orElseThrow(
                () -> new DataNotFoundException(roleName + " role is not found")
        );
    }
}
