package com.khai.edu.knysh.provide_and_order_services.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
public class ServiceOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status_id")
    private ServiceOrderStatus status;
    private String title;
    private String description;
    @ManyToOne()
    @JoinColumn(name = "customer_id")
    private User customer;
    @ManyToOne()
    @JoinColumn(name = "specialist_id")
    private User specialist;
    @ManyToOne()
    @JoinColumn(name = "work_category_id")
    private WorkCategory workCategory;
    private double cost;

    public ServiceOrder() {
        status = ServiceOrderStatus.CREATED;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public User getSpecialist() {
        return specialist;
    }

    public void setSpecialist(User specialist) {
        this.specialist = specialist;
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

    public WorkCategory getWorkCategory() {
        return workCategory;
    }

    public void setWorkCategory(WorkCategory workCategory) {
        this.workCategory = workCategory;
    }

    @Override
    public String toString() {
        return "ServiceOrder [" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", customer=" + customer +
                ", specialist=" + specialist +
                ", workCategory=" + workCategory +
                ", cost=" + cost +
                ", status=" + status +
                ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServiceOrder)) return false;
        if (!super.equals(o)) return false;
        ServiceOrder that = (ServiceOrder) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }

}
