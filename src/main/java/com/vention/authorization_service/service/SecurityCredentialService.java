package com.vention.authorization_service.service;

import com.vention.authorization_service.domain.SecurityCredentialEntity;
import com.vention.authorization_service.dto.response.SecurityCredentialsResponseDTO;

public interface SecurityCredentialService {

    SecurityCredentialEntity saveCredentials(SecurityCredentialEntity securityCredentialEntity);

    SecurityCredentialsResponseDTO getByEmail(String email);
}
