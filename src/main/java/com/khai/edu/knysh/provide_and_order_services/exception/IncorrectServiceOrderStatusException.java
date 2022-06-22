package com.khai.edu.knysh.provide_and_order_services.exception;

public class IncorrectServiceOrderStatusException extends RuntimeException {
    public IncorrectServiceOrderStatusException(String message) {
        super(message);
    }

    public IncorrectServiceOrderStatusException() {
    }
}
