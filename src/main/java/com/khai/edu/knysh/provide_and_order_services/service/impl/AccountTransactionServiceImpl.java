package com.khai.edu.knysh.provide_and_order_services.service.impl;

import com.khai.edu.knysh.provide_and_order_services.entity.AccountTransaction;
import com.khai.edu.knysh.provide_and_order_services.entity.ServiceOrder;
import com.khai.edu.knysh.provide_and_order_services.entity.ServiceOrderStatus;
import com.khai.edu.knysh.provide_and_order_services.entity.User;
import com.khai.edu.knysh.provide_and_order_services.exception.LowUserBalanceException;
import com.khai.edu.knysh.provide_and_order_services.exception.ServiceOrderNotFoundException;
import com.khai.edu.knysh.provide_and_order_services.repository.AccountTransactionRepository;
import com.khai.edu.knysh.provide_and_order_services.repository.ServiceOrderRepository;
import com.khai.edu.knysh.provide_and_order_services.repository.UserRepository;
import com.khai.edu.knysh.provide_and_order_services.service.AccountTransactionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountTransactionServiceImpl extends AbstractService<AccountTransaction> implements AccountTransactionService {

    private final AccountTransactionRepository accountTransactionRepository;
    private final UserRepository userRepository;
    private final ServiceOrderRepository serviceOrderRepository;

    public AccountTransactionServiceImpl(AccountTransactionRepository accountTransactionRepository, UserRepository userRepository, ServiceOrderRepository serviceOrderRepository) {
        super(accountTransactionRepository);
        this.accountTransactionRepository = accountTransactionRepository;
        this.userRepository = userRepository;
        this.serviceOrderRepository = serviceOrderRepository;
    }

    @Override
    public Double findSumOfAmountByUser(User user) {
        Double sum = accountTransactionRepository.findSumOfAmountByUser(user);
        return sum != null ? sum : 0;
    }

    @Override
    public long countByUser(User user) {
        return accountTransactionRepository.countByUser(user);
    }

    @Override
    public List<AccountTransaction> findAllByUser(User user) {
        return accountTransactionRepository.findAllByUser(user);
    }

    @Override
    public void payForServiceOrder(long userId, long serviceOrderId) {
        User user = getUser(userId);
        Double userBalance = accountTransactionRepository.findSumOfAmountByUser(user);
        AccountTransaction accountTransaction = new AccountTransaction();
        accountTransaction.setUser(user);
        Optional<ServiceOrder> orderOptional = serviceOrderRepository.findById(serviceOrderId);
        ServiceOrder order = orderOptional.orElseThrow(ServiceOrderNotFoundException::new);
        if (userBalance < order.getCost()) {
            throw new LowUserBalanceException();
        }
        accountTransaction.setAmount(-order.getCost());
        accountTransactionRepository.save(accountTransaction);
        accountTransaction = new AccountTransaction();
        accountTransaction.setUser(order.getSpecialist());
        accountTransaction.setAmount(order.getCost());
        accountTransactionRepository.save(accountTransaction);
        serviceOrderRepository.setStatusToServiceOrder(ServiceOrderStatus.COMPLETED, serviceOrderId);
    }

    @Override
    public void transferBetweenAccounts(long fromAccountOfUserId, long toAccountOfUserId, double amount) {
        User user = getUser(fromAccountOfUserId);
        Double userBalance = accountTransactionRepository.findSumOfAmountByUser(user);
        if (userBalance < amount) {
            return;
        }
        AccountTransaction transferFromAccount = new AccountTransaction();
        transferFromAccount.setAmount(-amount);
        transferFromAccount.setUser(user);
        user = getUser(toAccountOfUserId);
        if (user == null) return;
        AccountTransaction transferToAccount = new AccountTransaction();
        transferFromAccount.setAmount(amount);
        transferFromAccount.setUser(user);
        accountTransactionRepository.saveAll(List.of(transferFromAccount, transferToAccount));
    }

    private User getUser(long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return null;
        }
        return userOptional.get();
    }
}
