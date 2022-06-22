package com.khai.edu.knysh.provide_and_order_services.service;

import com.khai.edu.knysh.provide_and_order_services.entity.Feedback;
import com.khai.edu.knysh.provide_and_order_services.entity.ServiceOrder;
import com.khai.edu.knysh.provide_and_order_services.entity.User;

import java.util.List;
import java.util.Optional;

public interface FeedbackService extends GenericService<Feedback> {

    Optional<Feedback> findByServiceOrderAndUser(ServiceOrder serviceOrder, User user);

    List<Feedback> findAllAboutSpecialist(User specialist);

    List<Feedback> findAllAboutCustomer(User customer);

    List<Feedback> findAllCreatedBySpecialist(User specialist);

    List<Feedback> findAllCreatedByCustomer(User customer);

    Integer findAverageRatingAboutSpecialist(User specialist);

    Integer findAverageRatingAboutCustomer(User customer);
}
