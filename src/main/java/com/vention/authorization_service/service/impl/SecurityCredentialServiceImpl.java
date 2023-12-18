package com.vention.authorization_service.service.impl;

import com.vention.authorization_service.domain.SecurityCredentialEntity;
import com.vention.authorization_service.dto.response.SecurityCredentialsResponseDTO;
import com.vention.authorization_service.repository.SecurityCredentialRepository;
import com.vention.authorization_service.service.SecurityCredentialService;
import com.vention.general.lib.exceptions.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SecurityCredentialServiceImpl implements SecurityCredentialService {

    private final SecurityCredentialRepository securityCredentialRepository;

    @Override
    public SecurityCredentialEntity saveCredentials(SecurityCredentialEntity securityCredentialEntity) {
        return securityCredentialRepository.save(securityCredentialEntity);
    }

    @Override
    public SecurityCredentialsResponseDTO getByEmail(String email) {
        SecurityCredentialEntity securityCredentials = securityCredentialRepository.findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException("Security credentials not found with email: " + email));
        return new SecurityCredentialsResponseDTO(email, List.of(securityCredentials.getRole().getName()));
    }
}
