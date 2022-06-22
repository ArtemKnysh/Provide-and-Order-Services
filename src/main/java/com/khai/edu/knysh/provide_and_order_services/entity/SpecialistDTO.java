package com.khai.edu.knysh.provide_and_order_services.entity;

public class SpecialistDTO extends UserDTO {

    private Integer averageRating;

    public SpecialistDTO(User user, Integer averageRating) {
        super(user);
        this.averageRating = averageRating;
    }

    public SpecialistDTO(User user) {
        super(user);
    }

    public SpecialistDTO() {
    }

    public Integer getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Integer averageRating) {
        this.averageRating = averageRating;
    }

    @Override
    public String toString() {
        return "SpecialistDTO{" +
                "id=" + getId() +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", phoneNumber='" + getPhoneNumber() + '\'' +
                ", password='" + getPassword() + '\'' +
                ", role=" + getRole() +
                ", averageRating=" + averageRating +
                '}';
    }

}
