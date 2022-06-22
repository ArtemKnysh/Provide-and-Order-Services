package com.khai.edu.knysh.provide_and_order_services.exception;

public class ServiceOrderNotFoundException extends RuntimeException {
    public ServiceOrderNotFoundException(String message) {
        super(message);
    }

    public ServiceOrderNotFoundException() {
    }
}
