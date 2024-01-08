package com.vention.authorization_service.config;

import com.vention.authorization_service.dto.response.GlobalResponseDTO;
import com.vention.authorization_service.exception.ConfirmationTokenExpiredException;
import com.vention.authorization_service.exception.DuplicateDataException;
import com.vention.authorization_service.exception.FileConvertingException;
import com.vention.authorization_service.exception.InvalidFileTypeException;
import com.vention.authorization_service.exception.LoginFailedException;
import com.vention.general.lib.exceptions.BadRequestException;
import com.vention.general.lib.exceptions.DataNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = DataNotFoundException.class)
    public ResponseEntity<GlobalResponseDTO> apiExceptionHandler(DataNotFoundException e) {
        log.warn(e.getMessage());
        return getResponse(e.getMessage(), HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler(value = {DuplicateDataException.class, InvalidFileTypeException.class, BadRequestException.class})
    public ResponseEntity<GlobalResponseDTO> apiExceptionHandler(RuntimeException e) {
        log.warn(e.getMessage());
        return getResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(value = ConfirmationTokenExpiredException.class)
    public ResponseEntity<GlobalResponseDTO> apiExceptionHandler(ConfirmationTokenExpiredException e) {
        log.warn(e.getMessage());
        return getResponse(e.getMessage(), HttpStatus.UNAUTHORIZED.value());
    }

    @ExceptionHandler(value = FileConvertingException.class)
    public ResponseEntity<GlobalResponseDTO> apiExceptionHandler(FileConvertingException e) {
        log.warn(e.getMessage());
        return getResponse(e.getMessage(), HttpStatus.NOT_ACCEPTABLE.value());
    }

    @ExceptionHandler(value = LoginFailedException.class)
    public ResponseEntity<GlobalResponseDTO> apiExceptionHandler(LoginFailedException e) {
        log.warn(e.getMessage());
        return getResponse(e.getMessage(), HttpStatus.FORBIDDEN.value());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<GlobalResponseDTO> apiExceptionHandler(MethodArgumentNotValidException e) {
        log.warn(e.getMessage());
        return getResponse("Validation failed", HttpStatus.BAD_REQUEST.value());
    }
    private ResponseEntity<GlobalResponseDTO> getResponse(String message, int status) {
        return ResponseEntity
                .status(status)
                .body(GlobalResponseDTO.builder()
                        .status(status)
                        .message(message)
                        .time(ZonedDateTime.now())
                        .build());
    }
}
