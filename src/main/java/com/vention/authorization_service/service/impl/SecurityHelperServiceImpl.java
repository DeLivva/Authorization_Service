package com.vention.authorization_service.service.impl;

import com.vention.authorization_service.dto.response.UserResponseDTO;
import com.vention.authorization_service.exception.ConfirmationTokenExpiredException;
import com.vention.authorization_service.mapper.UserMapper;
import com.vention.authorization_service.service.SecurityHelperService;
import com.vention.authorization_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityHelperServiceImpl implements SecurityHelperService {

    private final UserMapper userMapper;

    private final UserService userService;

    @Override
    public UserResponseDTO getCurrentUser() {
        var currentUser = SecurityContextHolder.getContext().getAuthentication();
        if (!currentUser.isAuthenticated()) {
            throw new ConfirmationTokenExpiredException("Unauthenticated user!");
        }

        return userMapper.mapEntityToResponseDto(userService.getByEmail(currentUser.getName()));
    }
}
