package com.vention.authorization_service.service.impl;

import com.vention.authorization_service.domain.SecurityCredentialEntity;
import com.vention.authorization_service.repository.SecurityCredentialRepository;
import com.vention.authorization_service.service.SecurityCredentialService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityCredentialServiceImpl implements SecurityCredentialService {

    private final SecurityCredentialRepository securityCredentialRepository;

    @Override
    public SecurityCredentialEntity saveCredentials(SecurityCredentialEntity securityCredentialEntity) {
        return securityCredentialRepository.save(securityCredentialEntity);
    }
}
