package com.khai.edu.knysh.provide_and_order_services.controller;

import com.khai.edu.knysh.provide_and_order_services.entity.AccountTransaction;
import com.khai.edu.knysh.provide_and_order_services.entity.AccountTransactionWithBalance;
import com.khai.edu.knysh.provide_and_order_services.entity.ServiceOrder;
import com.khai.edu.knysh.provide_and_order_services.entity.ServiceOrderStatus;
import com.khai.edu.knysh.provide_and_order_services.entity.User;
import com.khai.edu.knysh.provide_and_order_services.exception.AccountTransactionNotFoundException;
import com.khai.edu.knysh.provide_and_order_services.exception.IncorrectServiceOrderStatusException;
import com.khai.edu.knysh.provide_and_order_services.exception.UserNotFoundException;
import com.khai.edu.knysh.provide_and_order_services.service.AccountTransactionService;
import com.khai.edu.knysh.provide_and_order_services.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/account-transaction")
public class AccountTransactionController {

    private final AccountTransactionService accountTransactionService;
    private final UserService userService;

    public AccountTransactionController(AccountTransactionService accountTransactionService, @Qualifier("userServiceImpl") UserService userService) {
        this.accountTransactionService = accountTransactionService;
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public AccountTransaction get(@PathVariable Long id) {
        return accountTransactionService.findById(id).orElseThrow(AccountTransactionNotFoundException::new);
    }

    @PostMapping
    public AccountTransaction create(@RequestBody AccountTransaction accountTransaction) {
        return accountTransactionService.save(accountTransaction);
    }

    @GetMapping("/list")
    public List<AccountTransaction> list() {
        return accountTransactionService.findAll();
    }

    @PutMapping("/edit")
    public void edit(@RequestParam AccountTransaction accountTransaction) {
        accountTransactionService.save(accountTransaction);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        accountTransactionService.deleteById(id);
    }

    @PostMapping("/user/top-up")
    public AccountTransactionWithBalance topUpUserAccount(@RequestParam Long userId, @RequestParam double amount) {
        User user = userService.findById(userId).orElseThrow(UserNotFoundException::new);
        AccountTransaction accountTransaction = new AccountTransaction();
        accountTransaction.setUser(user);
        accountTransaction.setAmount(amount);
        accountTransaction = accountTransactionService.save(accountTransaction);
        AccountTransactionWithBalance result = new AccountTransactionWithBalance(accountTransaction);
        Double balance = accountTransactionService.findSumOfAmountByUser(user);
        result.setBalance(balance);
        return result;
    }

    @PostMapping("/service-order/pay")
    public void payForServiceOrder(@RequestParam ServiceOrder serviceOrder, @RequestParam User user) {
        if (!serviceOrder.getCustomer().equals(user)) {
            throw new IllegalArgumentException();
        }
        double userBalance = accountTransactionService.findSumOfAmountByUser(user);
        if (userBalance < serviceOrder.getCost()) {
            throw new IllegalStateException();
        }
        if (serviceOrder.getStatus() != ServiceOrderStatus.WAIT_FOR_PAYMENT) {
            throw new IncorrectServiceOrderStatusException();
        }
        accountTransactionService.payForServiceOrder(user.getId(), serviceOrder.getId());
    }

    @PostMapping("/user/transfer")
    public void transferBetweenAccounts(@RequestParam User formAccountOfUser, @RequestParam User toAccountOfUser, @RequestParam double amount) {
        accountTransactionService.transferBetweenAccounts(formAccountOfUser.getId(), toAccountOfUser.getId(), amount);
    }

}
