package com.vention.authorization_service.config;

import com.vention.authorization_service.dto.response.GlobalResponseDTO;
import com.vention.authorization_service.exception.DataNotFoundException;
import com.vention.authorization_service.exception.DuplicateDataException;
import com.vention.authorization_service.exception.InvalidFileTypeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = DataNotFoundException.class)
    public ResponseEntity<GlobalResponse> apiExceptionHandler(DataNotFoundException e) {
        log.warn(e.getMessage());
        return getResponse(e.getMessage(), HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler(value = {DuplicateDataException.class, InvalidFileTypeException.class})
    public ResponseEntity<GlobalResponseDTO> apiExceptionHandler(RuntimeException e) {
        log.warn(e.getMessage());
        return getResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(value = ConfirmationTokenExpiredException.class)
    public ResponseEntity<GlobalResponse> apiExceptionHandler(ConfirmationTokenExpiredException e) {
        log.warn(e.getMessage());
        return getResponse(e.getMessage(), HttpStatus.UNAUTHORIZED.value());
    }

    private ResponseEntity<GlobalResponse> getResponse(String message, int status) {
        return ResponseEntity
                .status(status)
                .body(GlobalResponseDTO.builder()
                        .status(status)
                        .message(message)
                        .time(ZonedDateTime.now())
                        .build());
    }
}
