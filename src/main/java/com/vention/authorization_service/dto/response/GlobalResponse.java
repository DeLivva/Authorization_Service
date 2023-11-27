package com.vention.authorization_service.dto.response;

import lombok.*;

import java.time.ZonedDateTime;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GlobalResponse {
    private int status;
    private String message;
    private ZonedDateTime time;
}
