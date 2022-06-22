package com.khai.edu.knysh.provide_and_order_services.service.impl;

import com.khai.edu.knysh.provide_and_order_services.entity.ServiceOrder;
import com.khai.edu.knysh.provide_and_order_services.entity.ServiceOrderStatus;
import com.khai.edu.knysh.provide_and_order_services.entity.User;
import com.khai.edu.knysh.provide_and_order_services.repository.OfferServiceOrderRepository;
import com.khai.edu.knysh.provide_and_order_services.repository.ServiceOrderRepository;
import com.khai.edu.knysh.provide_and_order_services.service.ServiceOrderService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class ServiceOrderServiceImpl extends AbstractService<ServiceOrder> implements ServiceOrderService {

    private final ServiceOrderRepository serviceOrderRepository;
    private final List<ServiceOrderStatus> statusesMoreThenPaid;
    private final OfferServiceOrderRepository offerRepository;

    protected ServiceOrderServiceImpl(ServiceOrderRepository repository, OfferServiceOrderRepository offerRepository) {
        super(repository);
        serviceOrderRepository = repository;
        this.offerRepository = offerRepository;
        ServiceOrderStatus status = ServiceOrderStatus.PAID;
        statusesMoreThenPaid = Arrays.asList(ServiceOrderStatus.values()).subList(status.ordinal() + 1, ServiceOrderStatus.values().length);
    }

    @Override
    public void cancelServiceOrder(long serviceOrderId) {
        serviceOrderRepository.setStatusToServiceOrder(ServiceOrderStatus.CANCELLED, serviceOrderId);
    }

    @Override
    public void setStatusToServiceOrder(ServiceOrderStatus status, long serviceOrderId) {
        serviceOrderRepository.setStatusToServiceOrder(status, serviceOrderId);
    }

    @Override
    public void setCostToServiceOrder(double cost, long serviceOrderId) {
        serviceOrderRepository.setCostToServiceOrder(cost, serviceOrderId);
    }

    @Override
    public void setDescriptionToServiceOrder(String description, long serviceOrderId) {
        serviceOrderRepository.setDescriptionToServiceOrder(description, serviceOrderId);
    }

    @Override
    public void setSpecialistToServiceOrder(User specialist, long serviceOrderId) {
        serviceOrderRepository.setSpecialistToServiceOrder(specialist, serviceOrderId);
        serviceOrderRepository.setStatusToServiceOrder(ServiceOrderStatus.IN_WORK, serviceOrderId);
        offerRepository.deleteAllByServiceOrder_Id(serviceOrderId);
    }

    @Override
    public List<ServiceOrder> findAllBySpecialistAndStatusIn(User specialist, List<ServiceOrderStatus> statuses) {
        return serviceOrderRepository.findAllBySpecialistAndStatusIn(specialist, statuses);
    }

    @Override
    public List<ServiceOrder> findAllBySpecialistAndStatusMoreThenPaid(User specialist) {
        return serviceOrderRepository.findAllBySpecialistAndStatusIn(specialist, statusesMoreThenPaid);
    }

    @Override
    public List<ServiceOrder> findAllBySpecialistAndCustomerAndStatusIn(User specialist, User customer, List<ServiceOrderStatus> statuses) {
        return serviceOrderRepository.findAllBySpecialistAndCustomerAndStatusIn(specialist, customer, statuses);
    }

    @Override
    public List<ServiceOrder> findAllBySpecialistAndCustomerAndStatusMoreThenPaid(User specialist, User customer) {
        return serviceOrderRepository.findAllBySpecialistAndCustomerAndStatusIn(specialist, customer, statusesMoreThenPaid);
    }

    @Override
    public List<ServiceOrder> findAllBySpecialistAndCustomerAndStatusCompleted(User specialist, User customer) {
        return serviceOrderRepository.findAllBySpecialistAndCustomerAndStatusIn(specialist, customer, Collections.singletonList(ServiceOrderStatus.COMPLETED));
    }

    @Override
    public List<ServiceOrder> findAllByCustomer(User customer) {
        return serviceOrderRepository.findAllByCustomer(customer);
    }

    @Override
    public List<ServiceOrder> findAllByCustomerAndSpecialistIsNull(User customer) {
        return serviceOrderRepository.findAllByCustomerAndSpecialistIsNull(customer);
    }

    @Override
    public List<ServiceOrder> findAllNotCancelledByCustomer(User customer) {
        return serviceOrderRepository.findAllByCustomerAndStatusNot(customer, ServiceOrderStatus.CANCELLED);
    }

    @Override
    public void removeSpecialistFromServiceOrder(long serviceOrderId) {
        serviceOrderRepository.removeSpecialistFromServiceOrder(serviceOrderId);
    }

    @Override
    public long countByCustomer(User customer) {
        return serviceOrderRepository.countByCustomer(customer);
    }

    @Override
    public long countBySpecialistAndStatusIn(User specialist, List<ServiceOrderStatus> statuses) {
        return serviceOrderRepository.countBySpecialistAndStatusIn(specialist, statuses);
    }

    @Override
    public long countBySpecialistAndStatusMoreThenPaid(User specialist) {
        return serviceOrderRepository.countBySpecialistAndStatusIn(specialist, statusesMoreThenPaid);
    }

    @Override
    public List<ServiceOrder> findAllBySpecialistAndCustomer(User specialist, User customer) {
        return serviceOrderRepository.findAllBySpecialistAndCustomer(specialist, customer);
    }
}
