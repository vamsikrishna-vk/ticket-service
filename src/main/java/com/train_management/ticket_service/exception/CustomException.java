package com.train_management.ticket_service.exception;

public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }
}
