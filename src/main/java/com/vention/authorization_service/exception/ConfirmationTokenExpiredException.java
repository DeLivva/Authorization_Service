package com.vention.authorization_service.exception;

public class ConfirmationTokenExpiredException extends RuntimeException{
    public ConfirmationTokenExpiredException(String message) {
        super(message);
    }
}