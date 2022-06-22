package com.khai.edu.knysh.provide_and_order_services.exception;

public class LowUserBalanceException extends RuntimeException {
    public LowUserBalanceException(String message) {
        super(message);
    }

    public LowUserBalanceException() {
    }
}
