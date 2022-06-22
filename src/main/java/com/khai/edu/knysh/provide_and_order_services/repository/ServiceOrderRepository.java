package com.khai.edu.knysh.provide_and_order_services.repository;

import com.khai.edu.knysh.provide_and_order_services.entity.ServiceOrder;
import com.khai.edu.knysh.provide_and_order_services.entity.ServiceOrderStatus;
import com.khai.edu.knysh.provide_and_order_services.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface ServiceOrderRepository extends CrudRepository<ServiceOrder, Long> {

    @Modifying
    @Query("update ServiceOrder so set so.status = :status where so.id = :serviceOrderId")
    void setStatusToServiceOrder(ServiceOrderStatus status, long serviceOrderId);

    @Modifying
    @Query("update ServiceOrder so set so.cost = :cost where so.id = :serviceOrderId")
    void setCostToServiceOrder(double cost, long serviceOrderId);

    @Modifying
    @Query("update ServiceOrder so set so.description = :description where so.id = :serviceOrderId")
    void setDescriptionToServiceOrder(String description, long serviceOrderId);

    @Modifying
    @Query("update ServiceOrder so set so.specialist = :specialist where so.id = :serviceOrderId")
    void setSpecialistToServiceOrder(User specialist, long serviceOrderId);

    List<ServiceOrder> findAllBySpecialistAndStatusIn(User specialist, List<ServiceOrderStatus> statuses);

    List<ServiceOrder> findAllBySpecialistAndCustomerAndStatusIn(User specialist, User customer, List<ServiceOrderStatus> statuses);

    List<ServiceOrder> findAllByCustomer(User customer);

    List<ServiceOrder> findAllByCustomerAndSpecialistIsNull(User customer);

    List<ServiceOrder> findAllByCustomerAndStatusNot(User customer, ServiceOrderStatus status);

    @Modifying
    @Query("update ServiceOrder so set so.specialist = null where so.id = :serviceOrderId")
    void removeSpecialistFromServiceOrder(long serviceOrderId);

    long countByCustomer(User customer);

    long countBySpecialistAndStatusIn(User customer, List<ServiceOrderStatus> statuses);

    List<ServiceOrder> findAllBySpecialistAndCustomer(User specialist, User customer);
}
