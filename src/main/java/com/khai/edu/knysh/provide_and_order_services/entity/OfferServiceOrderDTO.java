package com.khai.edu.knysh.provide_and_order_services.entity;

import java.util.Objects;

public class OfferServiceOrderDTO {

    private long id;
    private String customerFullName;
    private String customerPhoneNumber;
    private String customerEmail;
    private Long serviceOrderId;
    private String serviceOrderTitle;
    private Double serviceOrderCost;
    private String workCategoryName;
    private Long specialistId;
    private Long customerId;

    public OfferServiceOrderDTO(OfferServiceOrder offer) {
        id = offer.getId();
        customerFullName = offer.getServiceOrder().getCustomer().getFullName();
        customerPhoneNumber = offer.getServiceOrder().getCustomer().getPhoneNumber();
        customerEmail = offer.getServiceOrder().getCustomer().getEmail();
        serviceOrderId = offer.getServiceOrder().getId();
        serviceOrderTitle = offer.getServiceOrder().getTitle();
        serviceOrderCost = offer.getServiceOrder().getCost();
        workCategoryName = offer.getServiceOrder().getWorkCategory().getName();
        specialistId = offer.getSpecialist().getId();
        customerId = offer.getServiceOrder().getCustomer().getId();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCustomerFullName() {
        return customerFullName;
    }

    public void setCustomerFullName(String customerFullName) {
        this.customerFullName = customerFullName;
    }

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public Long getServiceOrderId() {
        return serviceOrderId;
    }

    public void setServiceOrderId(Long serviceOrderId) {
        this.serviceOrderId = serviceOrderId;
    }

    public String getServiceOrderTitle() {
        return serviceOrderTitle;
    }

    public void setServiceOrderTitle(String serviceOrderTitle) {
        this.serviceOrderTitle = serviceOrderTitle;
    }

    public Double getServiceOrderCost() {
        return serviceOrderCost;
    }

    public void setServiceOrderCost(Double serviceOrderCost) {
        this.serviceOrderCost = serviceOrderCost;
    }

    public String getWorkCategoryName() {
        return workCategoryName;
    }

    public void setWorkCategoryName(String workCategoryName) {
        this.workCategoryName = workCategoryName;
    }

    public Long getSpecialistId() {
        return specialistId;
    }

    public void setSpecialistId(Long specialistId) {
        this.specialistId = specialistId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OfferServiceOrderDTO)) return false;
        OfferServiceOrderDTO that = (OfferServiceOrderDTO) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

