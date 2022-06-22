package com.khai.edu.knysh.provide_and_order_services.entity;

import java.util.Arrays;
import java.util.List;

public enum ServiceOrderStatus {

    CREATED, CANCELLED, IN_WORK, WAIT_FOR_CUSTOMER_APPROVE, WAIT_FOR_PAYMENT, PAID, COMPLETED;

    public List<ServiceOrderStatus> getNextStatuses() {
        return Arrays.asList(values()).subList(ordinal() + 1, values().length);
    }
}
