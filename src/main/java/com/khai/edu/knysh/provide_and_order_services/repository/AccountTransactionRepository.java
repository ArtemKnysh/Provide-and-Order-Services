package com.khai.edu.knysh.provide_and_order_services.repository;

import com.khai.edu.knysh.provide_and_order_services.entity.AccountTransaction;
import com.khai.edu.knysh.provide_and_order_services.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface AccountTransactionRepository extends CrudRepository<AccountTransaction, Long> {

    @Query("select sum(at.amount) from AccountTransaction at where at.user = :user")
    Double findSumOfAmountByUser(User user);

    long countByUser(User user);

    List<AccountTransaction> findAllByUser(User user);

}
