package com.khai.edu.knysh.provide_and_order_services.exception;

public class FeedbackNotFoundException extends RuntimeException {
    public FeedbackNotFoundException(String message) {
        super(message);
    }

    public FeedbackNotFoundException() {
    }
}
