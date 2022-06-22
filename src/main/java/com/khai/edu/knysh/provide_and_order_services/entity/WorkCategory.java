package com.khai.edu.knysh.provide_and_order_services.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Objects;
import java.util.Set;

@Entity
public class WorkCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String icon;
    @ManyToOne
    private WorkCategory parent;
    @OneToMany(mappedBy = "parent")
    private Set<WorkCategory> subWorkCategories;
    @ManyToMany
    @JoinTable(
            name = "specialist_work_categories",
            joinColumns = @JoinColumn(name = "work_category_id"),
            inverseJoinColumns = @JoinColumn(name = "specialist_id")
    )
    private Set<User> specialists;
    @OneToMany(mappedBy = "workCategory")
    private Set<ServiceOrder> serviceOrders;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public WorkCategory getParent() {
        return parent;
    }

    public void setParent(WorkCategory parent) {
        this.parent = parent;
    }

    public Set<WorkCategory> getSubWorkCategories() {
        return subWorkCategories;
    }

    public void setSubWorkCategories(Set<WorkCategory> subWorkCategories) {
        this.subWorkCategories = subWorkCategories;
    }

    public Set<User> getSpecialists() {
        return specialists;
    }

    public void setSpecialists(Set<User> specialists) {
        this.specialists = specialists;
    }

    public Set<ServiceOrder> getServiceOrders() {
        return serviceOrders;
    }

    public void setServiceOrders(Set<ServiceOrder> serviceOrders) {
        this.serviceOrders = serviceOrders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WorkCategory)) return false;
        WorkCategory that = (WorkCategory) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "WorkCategory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", parent=" + parent +
                '}';
    }
}
