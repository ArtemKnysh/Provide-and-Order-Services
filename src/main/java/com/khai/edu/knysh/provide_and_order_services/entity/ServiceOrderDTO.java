package com.khai.edu.knysh.provide_and_order_services.entity;

import com.khai.edu.knysh.provide_and_order_services.service.UserService;
import com.khai.edu.knysh.provide_and_order_services.service.WorkCategoryService;

public class ServiceOrderDTO {

    private long id;
    private ServiceOrderStatus status;
    private String title;
    private String description;
    private long customerId;
    private long specialistId;
    private long workCategoryId;
    private double cost;

    public ServiceOrderDTO() {
        status = ServiceOrderStatus.CREATED;
    }

    public ServiceOrderDTO(ServiceOrder serviceOrder) {
        id = serviceOrder.getId();
        status = serviceOrder.getStatus();
        title = serviceOrder.getTitle();
        description = serviceOrder.getDescription();
        if (serviceOrder.getCustomer() != null) {
            customerId = serviceOrder.getCustomer().getId();
        }
        if (serviceOrder.getSpecialist() != null) {
            specialistId = serviceOrder.getSpecialist().getId();
        }
        if (serviceOrder.getWorkCategory() != null) {
            workCategoryId = serviceOrder.getWorkCategory().getId();
        }
        cost = serviceOrder.getCost();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public long getSpecialistId() {
        return specialistId;
    }

    public void setSpecialistId(long specialistId) {
        this.specialistId = specialistId;
    }

    public ServiceOrderStatus getStatus() {
        return status;
    }

    public void setStatus(ServiceOrderStatus status) {
        this.status = status;
    }

    public boolean isCanBeCancelled() {
        return getStatus().ordinal() < ServiceOrderStatus.CANCELLED.ordinal();
    }

    public long getWorkCategoryId() {
        return workCategoryId;
    }

    public void setWorkCategoryId(long workCategoryId) {
        this.workCategoryId = workCategoryId;
    }

    @Override
    public String toString() {
        return "ServiceOrderDTO [" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", customer=" + customerId +
                ", specialist=" + specialistId +
                ", workCategory=" + workCategoryId +
                ", cost=" + cost +
                ", status=" + status +
                ']';
    }

    public ServiceOrder toEntity(WorkCategoryService workCategoryService, UserService userService) {
        ServiceOrder serviceOrder = new ServiceOrder();
        serviceOrder.setId(id);
        serviceOrder.setTitle(title);
        serviceOrder.setDescription(description);
        userService.findById(specialistId).ifPresent(serviceOrder::setSpecialist);
        userService.findById(customerId).ifPresent(serviceOrder::setCustomer);
        serviceOrder.setStatus(status);
        serviceOrder.setCost(cost);
        workCategoryService.findById(workCategoryId).ifPresent(serviceOrder::setWorkCategory);
        return serviceOrder;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
