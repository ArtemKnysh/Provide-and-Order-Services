package com.khai.edu.knysh.provide_and_order_services.entity;

public class AccountTransactionWithBalance extends AccountTransaction {

    private Double balance;

    public AccountTransactionWithBalance(AccountTransaction accountTransaction) {
        this.setId(accountTransaction.getId());
        this.setAmount(accountTransaction.getAmount());
        this.setUser(accountTransaction.getUser());
        this.setCreatedAt(accountTransaction.getCreatedAt());
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
