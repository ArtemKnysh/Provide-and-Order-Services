package com.khai.edu.knysh.provide_and_order_services.service.impl;

import com.khai.edu.knysh.provide_and_order_services.entity.Feedback;
import com.khai.edu.knysh.provide_and_order_services.entity.ServiceOrder;
import com.khai.edu.knysh.provide_and_order_services.entity.User;
import com.khai.edu.knysh.provide_and_order_services.repository.FeedbackRepository;
import com.khai.edu.knysh.provide_and_order_services.service.FeedbackService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeedbackServiceImpl extends AbstractService<Feedback> implements FeedbackService {

    private final FeedbackRepository feedbackRepository;

    protected FeedbackServiceImpl(FeedbackRepository feedbackRepository) {
        super(feedbackRepository);
        this.feedbackRepository = feedbackRepository;
    }

    @Override
    public Optional<Feedback> findByServiceOrderAndUser(ServiceOrder serviceOrder, User user) {
        return feedbackRepository.findByServiceOrderAndUser(serviceOrder, user);
    }

    @Override
    public List<Feedback> findAllAboutSpecialist(User specialist) {
        return feedbackRepository.findAllAboutSpecialist(specialist);
    }

    @Override
    public List<Feedback> findAllAboutCustomer(User customer) {
        return feedbackRepository.findAllAboutCustomer(customer);
    }

    @Override
    public List<Feedback> findAllCreatedBySpecialist(User specialist) {
        return feedbackRepository.findAllByUser(specialist);
    }

    @Override
    public List<Feedback> findAllCreatedByCustomer(User customer) {
        return feedbackRepository.findAllByUser(customer);
    }

    @Override
    public Integer findAverageRatingAboutSpecialist(User specialist) {
        return feedbackRepository.findAverageRatingAboutSpecialist(specialist);
    }

    @Override
    public Integer findAverageRatingAboutCustomer(User customer) {
        return feedbackRepository.findAverageRatingAboutCustomer(customer);
    }
}
