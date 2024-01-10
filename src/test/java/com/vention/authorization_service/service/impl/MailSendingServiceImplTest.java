package com.vention.authorization_service.service.impl;

import com.vention.authorization_service.domain.ConfirmationToken;
import com.vention.general.lib.exceptions.DataNotFoundException;
import com.vention.authorization_service.service.rabbitmq.producer.RabbitMQProducer;
import com.vention.authorization_service.repository.ConfirmationTokenRepository;
import com.vention.authorization_service.service.MailSendingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MailSendingServiceImplTest {
    @Mock
    private ConfirmationTokenRepository confirmationTokenRepository;
    @Mock
    private RabbitMQProducer rabbitMQProducer;
    private MailSendingService mailSendingService;
    private ConfirmationToken testToken;

    @BeforeEach
    void setUp() {
        mailSendingService = new MailSendingServiceImpl(confirmationTokenRepository, rabbitMQProducer);
        testToken = mock();
    }

    @Test
    void testGetConfirmationToken() {
        // given
        // when
        doReturn(Optional.of(testToken)).when(confirmationTokenRepository).findByToken(any(String.class));
        ConfirmationToken token = mailSendingService.getConfirmationToken("testToken");
        //then
        verify(confirmationTokenRepository, times(1)).findByToken(any());
        assertSame(testToken, token);
    }

    @Test
    void testGetConfirmationTokenNotFound() {
        // given
        // when
        doReturn(Optional.empty()).when(confirmationTokenRepository).findByToken(any());
        try {
            mailSendingService.getConfirmationToken("testToken");
            // then
        } catch (DataNotFoundException e) {
            // then
            assertEquals(e.getMessage(), "Token testToken not found");
            verify(confirmationTokenRepository, times(1)).findByToken(any());
        }
    }

    @Test
    void testSaveToken() {
        // given
        // when
        doReturn(testToken).when(confirmationTokenRepository).save(any());
        ConfirmationToken token = mailSendingService.saveToken(testToken);
        // then
        verify(confirmationTokenRepository, times(1)).save(any(ConfirmationToken.class));
        assertNotNull(token.getId());
        assertSame(testToken, token);
    }
}