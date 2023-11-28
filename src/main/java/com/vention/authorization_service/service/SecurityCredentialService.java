package com.vention.authorization_service.service;

import com.vention.authorization_service.domain.SecurityCredentialEntity;

public interface SecurityCredentialService {

    SecurityCredentialEntity saveCredentials(SecurityCredentialEntity securityCredentialEntity);
}
