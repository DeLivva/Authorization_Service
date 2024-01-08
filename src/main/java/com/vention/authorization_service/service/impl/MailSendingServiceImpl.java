package com.vention.authorization_service.service.impl;

import com.vention.authorization_service.domain.ConfirmationToken;
import com.vention.authorization_service.domain.UserEntity;
import com.vention.general.lib.exceptions.DataNotFoundException;
import com.vention.authorization_service.service.rabbitmq.producer.RabbitMQProducer;
import com.vention.authorization_service.repository.ConfirmationTokenRepository;
import com.vention.authorization_service.service.MailSendingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MailSendingServiceImpl implements MailSendingService {
    @Value("${token.expiration.seconds}")
    private Long tokenExpiry;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final RabbitMQProducer producer;

    @Override
    public void sendConfirmationToken(UserEntity user) {
        ConfirmationToken confirmationToken = ConfirmationToken.builder()
                .issuedAt(Timestamp.valueOf(LocalDateTime.now()))
                .expiredAt(Timestamp.valueOf(LocalDateTime.now().plusMinutes(tokenExpiry)))
                .user(user)
                .build();
        confirmationTokenRepository.save(confirmationToken);
        producer.sendMessage(user.getEmail(), confirmationToken.getToken());
    }

    @Override
    public ConfirmationToken getConfirmationToken(String token) {
        return confirmationTokenRepository.findByToken(token).orElseThrow(
                () -> new DataNotFoundException("Token " + token + " not found")
        );
    }

    @Override
    public ConfirmationToken saveToken(ConfirmationToken token) {
        return confirmationTokenRepository.save(token);
    }

}
