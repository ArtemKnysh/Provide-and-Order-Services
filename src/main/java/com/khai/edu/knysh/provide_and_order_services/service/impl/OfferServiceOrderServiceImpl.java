package com.khai.edu.knysh.provide_and_order_services.service.impl;

import com.khai.edu.knysh.provide_and_order_services.entity.OfferServiceOrder;
import com.khai.edu.knysh.provide_and_order_services.entity.User;
import com.khai.edu.knysh.provide_and_order_services.repository.OfferServiceOrderRepository;
import com.khai.edu.knysh.provide_and_order_services.service.OfferServiceOrderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfferServiceOrderServiceImpl extends AbstractService<OfferServiceOrder> implements OfferServiceOrderService {

    private final OfferServiceOrderRepository repository;

    protected OfferServiceOrderServiceImpl(OfferServiceOrderRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public OfferServiceOrder findBySpecialistIdAndServiceOrderId(Long userId, Long serviceOrderId) {
        return repository.findBySpecialist_IdAndServiceOrder_Id(userId, serviceOrderId);
    }

    @Override
    public List<OfferServiceOrder> findAllNotAnswered(User specialist) {
        return repository.findAllBySpecialistAndSpecialistAnswerIsNull(specialist);
    }

    @Override
    public List<OfferServiceOrder> findAllWithAnswer(User specialist, boolean answer) {
        return repository.findAllBySpecialistAndSpecialistAnswer(specialist, answer);
    }

}
