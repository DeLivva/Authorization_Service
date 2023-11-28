package com.vention.authorization_service.service.impl;

import com.vention.authorization_service.domain.SecurityCredentialEntity;
import com.vention.authorization_service.repository.SecurityCredentialRepository;
import com.vention.authorization_service.service.SecurityCredentialService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SecurityCredentialServiceImplTest {

    @Mock
    private SecurityCredentialRepository securityCredentialRepository;

    private SecurityCredentialService securityCredentialService;

    private SecurityCredentialEntity credentialEntity;

    @BeforeEach
    void setUp() {
        securityCredentialService = new SecurityCredentialServiceImpl(securityCredentialRepository);
        credentialEntity = mock();
    }

    @Test
    void testSaveCredentials() {
        // given
        // when
        doReturn(credentialEntity).when(securityCredentialRepository).save(any());
        SecurityCredentialEntity savedCredential = securityCredentialService.saveCredentials(credentialEntity);
        // then
        verify(securityCredentialRepository, times(1)).save(any());
        assertSame(credentialEntity, savedCredential);
    }
}