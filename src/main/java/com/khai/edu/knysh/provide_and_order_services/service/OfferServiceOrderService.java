package com.khai.edu.knysh.provide_and_order_services.service;

import com.khai.edu.knysh.provide_and_order_services.entity.OfferServiceOrder;
import com.khai.edu.knysh.provide_and_order_services.entity.User;

import java.util.List;

public interface OfferServiceOrderService extends GenericService<OfferServiceOrder> {

    OfferServiceOrder findBySpecialistIdAndServiceOrderId(Long userId, Long serviceOrderId);

    List<OfferServiceOrder> findAllNotAnswered(User specialist);

    List<OfferServiceOrder> findAllWithAnswer(User specialist, boolean answer);

}
