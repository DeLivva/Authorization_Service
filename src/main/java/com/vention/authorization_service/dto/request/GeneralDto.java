package com.vention.authorization_service.dto.request;

import com.vention.authorization_service.domain.NotificationType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GeneralDto<T> {
    private T body;
    private NotificationType type;
}
