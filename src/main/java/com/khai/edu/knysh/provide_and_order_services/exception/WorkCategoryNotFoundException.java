package com.khai.edu.knysh.provide_and_order_services.exception;

public class WorkCategoryNotFoundException extends RuntimeException {
    public WorkCategoryNotFoundException(String message) {
        super(message);
    }

    public WorkCategoryNotFoundException() {
    }
}
