package com.fiap.restaurant_api.domain.exception;

public class ValidationException
        extends BusinessException {

    public ValidationException(String message) {
        super(message);
    }
}
