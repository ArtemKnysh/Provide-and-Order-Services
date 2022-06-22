package com.khai.edu.knysh.provide_and_order_services.entity;

public class WorkCategoryChanged {

    private final long id;
    private String name;
    private long specialistId;

    public WorkCategoryChanged(WorkCategory workCategory, User specialist) {
        id = workCategory.getId();
        name = workCategory.getName();
        specialistId = specialist.getId();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSpecialistId() {
        return specialistId;
    }

    public void setSpecialistId(long specialistId) {
        this.specialistId = specialistId;
    }

}
