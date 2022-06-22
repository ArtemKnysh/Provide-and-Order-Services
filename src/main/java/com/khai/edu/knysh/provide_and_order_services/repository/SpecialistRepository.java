package com.khai.edu.knysh.provide_and_order_services.repository;

import com.khai.edu.knysh.provide_and_order_services.entity.ServiceOrder;
import com.khai.edu.knysh.provide_and_order_services.entity.User;
import com.khai.edu.knysh.provide_and_order_services.entity.UserRole;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Set;

@Repository
@Transactional
public interface SpecialistRepository extends UserRepository {

    @Query("select s from WorkCategory wc join wc.specialists s " +
            "where s.role = :userRole and wc.id = :workCategoryId " +
            "or (wc.parent is not null and wc.parent.id = :workCategoryId)")
    Set<User> findAllByRoleAndWorkCategoryId(UserRole userRole, long workCategoryId);

    Set<User> findAllByRoleAndOffers_ServiceOrderAndOffers_SpecialistAnswer(UserRole role, ServiceOrder serviceOrder, Boolean specialistAnswer);

    @Query("select u from User u join u.offers o " +
            "where u.role = :role and o.serviceOrder = :serviceOrder and (o.specialistAnswer is null or o.specialistAnswer = false)")
    Set<User> findAllWithOffers(UserRole role, ServiceOrder serviceOrder);

}
