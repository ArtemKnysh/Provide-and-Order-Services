package com.khai.edu.knysh.provide_and_order_services.service;

import com.khai.edu.knysh.provide_and_order_services.entity.ServiceOrder;
import com.khai.edu.knysh.provide_and_order_services.entity.ServiceOrderStatus;
import com.khai.edu.knysh.provide_and_order_services.entity.User;

import java.util.List;

public interface ServiceOrderService extends GenericService<ServiceOrder> {

    void cancelServiceOrder(long serviceOrderId);

    void setStatusToServiceOrder(ServiceOrderStatus status, long serviceOrderId);

    void setCostToServiceOrder(double cost, long serviceOrderId);

    void setDescriptionToServiceOrder(String description, long serviceOrderId);

    void setSpecialistToServiceOrder(User specialist, long serviceOrderId);

    List<ServiceOrder> findAllBySpecialistAndStatusIn(User specialist, List<ServiceOrderStatus> statuses);

    List<ServiceOrder> findAllBySpecialistAndStatusMoreThenPaid(User specialist);

    List<ServiceOrder> findAllBySpecialistAndCustomerAndStatusIn(User specialist, User customer, List<ServiceOrderStatus> statuses);

    List<ServiceOrder> findAllBySpecialistAndCustomerAndStatusMoreThenPaid(User specialist, User customer);

    List<ServiceOrder> findAllBySpecialistAndCustomerAndStatusCompleted(User specialist, User customer);

    List<ServiceOrder> findAllByCustomer(User customer);

    List<ServiceOrder> findAllByCustomerAndSpecialistIsNull(User customer);

    List<ServiceOrder> findAllNotCancelledByCustomer(User customer);

    void removeSpecialistFromServiceOrder(long serviceOrderId);

    long countByCustomer(User customer);

    long countBySpecialistAndStatusIn(User specialist, List<ServiceOrderStatus> statuses);

    long countBySpecialistAndStatusMoreThenPaid(User specialist);

    List<ServiceOrder> findAllBySpecialistAndCustomer(User specialist, User customer);
}
