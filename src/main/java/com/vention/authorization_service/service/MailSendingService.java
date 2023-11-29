package com.vention.authorization_service.service;

import com.vention.authorization_service.domain.ConfirmationToken;
import com.vention.authorization_service.domain.UserEntity;

public interface MailSendingService {
    void sendConfirmationToken(UserEntity userEntity);

    ConfirmationToken getConfirmationToken(String  token);

    ConfirmationToken saveToken(ConfirmationToken token);
}
