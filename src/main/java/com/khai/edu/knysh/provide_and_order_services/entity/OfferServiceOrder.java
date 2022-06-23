package com.khai.edu.knysh.provide_and_order_services.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name="offer_service_order", schema = "public")
public class OfferServiceOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "specialist_id")
    private User specialist;
    @ManyToOne
    @JoinColumn(name = "service_order_id")
    private ServiceOrder serviceOrder;
    @Column(name = "specialist_answer")
    private Boolean specialistAnswer;

    public OfferServiceOrder() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getSpecialist() {
        return specialist;
    }

    public void setSpecialist(User specialist) {
        this.specialist = specialist;
    }

    public ServiceOrder getServiceOrder() {
        return serviceOrder;
    }

    public void setServiceOrder(ServiceOrder serviceOrder) {
        this.serviceOrder = serviceOrder;
    }

    public Boolean getSpecialistAnswer() {
        return specialistAnswer;
    }

    public void setSpecialistAnswer(Boolean specialistAnswer) {
        this.specialistAnswer = specialistAnswer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OfferServiceOrder)) return false;
        OfferServiceOrder that = (OfferServiceOrder) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

