package com.khai.edu.knysh.provide_and_order_services.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name="account_transaction", schema = "public")
public class AccountTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;
    private double amount;
    @CreationTimestamp
    @Column(name = "create_time")
    private LocalDateTime createdAt;

    public AccountTransaction() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getAmountWithSign() {
        if (amount > 0) {
            return "+" + amount;
        } else {
            return String.valueOf(amount);
        }
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "AccountTransaction [" +
                "id=" + getId() +
                "userId=" + user +
                ", amount=" + amount +
                ", createdAt=" + createdAt +
                ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountTransaction)) return false;
        AccountTransaction that = (AccountTransaction) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
