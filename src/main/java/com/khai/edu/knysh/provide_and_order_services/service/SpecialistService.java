package com.khai.edu.knysh.provide_and_order_services.service;

import com.khai.edu.knysh.provide_and_order_services.entity.ServiceOrder;
import com.khai.edu.knysh.provide_and_order_services.entity.User;

import java.util.Set;

public interface SpecialistService extends UserService {

    Set<User> findAllByWorkCategoryId(long workCategoryId);

    User findSpecialistById(long specialistId);

    Set<User> findAllWithOffers(ServiceOrder serviceOrder);

    Set<User> findAllAcceptedOffers(ServiceOrder serviceOrder);

    Set<User> findAllWithOutOffers(long serviceOrderId);

}
