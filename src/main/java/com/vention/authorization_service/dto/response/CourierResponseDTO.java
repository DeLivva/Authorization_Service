package com.vention.authorization_service.dto.response;

import lombok.Data;

@Data
public class CourierResponseDTO extends UserResponseDTO {
    private Integer rate = 3;
}
