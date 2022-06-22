package com.khai.edu.knysh.provide_and_order_services.repository;

import com.khai.edu.knysh.provide_and_order_services.entity.OfferServiceOrder;
import com.khai.edu.knysh.provide_and_order_services.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface OfferServiceOrderRepository extends CrudRepository<OfferServiceOrder, Long> {

    OfferServiceOrder findBySpecialist_IdAndServiceOrder_Id(Long userId, Long serviceOrderId);

    List<OfferServiceOrder> findAllBySpecialistAndSpecialistAnswerIsNull(User specialist);

    List<OfferServiceOrder> findAllBySpecialistAndSpecialistAnswer(User specialist, boolean specialistAnswer);

    void deleteAllByServiceOrder_Id(Long serviceOrderId);

}
