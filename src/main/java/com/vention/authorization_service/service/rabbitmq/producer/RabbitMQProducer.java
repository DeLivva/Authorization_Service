package com.vention.authorization_service.service.rabbitmq.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vention.authorization_service.dto.NotificationDTO;
import com.vention.authorization_service.dto.request.ConfirmationTokenDto;
import com.vention.authorization_service.dto.request.GeneralDto;
import com.vention.authorization_service.domain.NotificationType;
import com.vention.general.lib.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RabbitMQProducer {

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger log = LoggerFactory.getLogger(RabbitMQProducer.class);

    public void sendMessage(String email, String token) {
        Map<String, Object> data = new HashMap<>() {{
            put("email", email);
            put("token", token);
        }};
        NotificationDTO notificationDTO = new NotificationDTO("Please confirm your account before moving forward", email, data);
        GeneralDto<NotificationDTO> generalDto = GeneralDto.<NotificationDTO>builder().body(notificationDTO).type(NotificationType.CONFIRMATION_TOKEN).build();
        try {
            String json = objectMapper.writeValueAsString(generalDto);
            rabbitTemplate.convertAndSend(
                    exchange,
                    routingKey,
                    json,
                    message -> {
                        MessageProperties properties = message.getMessageProperties();
                        properties.setContentType("application/json");
                        return message;
                    });
        } catch (IOException e) {
            log.error("Error occurred while sending confirmation token: ", e);
            throw new BadRequestException(e.getMessage());
        }
    }
}
