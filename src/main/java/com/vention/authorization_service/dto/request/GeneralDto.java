package com.vention.authorization_service.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GeneralDto<T> {
    private T data;
    private NotificationType type;
}
