package com.khai.edu.knysh.provide_and_order_services.service;

import com.khai.edu.knysh.provide_and_order_services.entity.AccountTransaction;
import com.khai.edu.knysh.provide_and_order_services.entity.User;

import java.util.List;

public interface AccountTransactionService extends GenericService<AccountTransaction> {

    Double findSumOfAmountByUser(User user);

    long countByUser(User user);

    List<AccountTransaction> findAllByUser(User user);

    void payForServiceOrder(long userId, long serviceOrderId);

    void transferBetweenAccounts(long fromAccountOfUserId, long toAccountOfUserId, double amount);

}
