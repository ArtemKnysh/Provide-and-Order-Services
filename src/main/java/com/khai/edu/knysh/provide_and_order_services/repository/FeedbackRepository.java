package com.khai.edu.knysh.provide_and_order_services.repository;

import com.khai.edu.knysh.provide_and_order_services.entity.Feedback;
import com.khai.edu.knysh.provide_and_order_services.entity.ServiceOrder;
import com.khai.edu.knysh.provide_and_order_services.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface FeedbackRepository extends CrudRepository<Feedback, Long> {

    Optional<Feedback> findByServiceOrderAndUser(ServiceOrder serviceOrder, User user);

    @Query("select f from Feedback f where f.serviceOrder.specialist = :specialist and f.user <> :specialist")
    List<Feedback> findAllAboutSpecialist(User specialist);

    @Query("select f from Feedback f where f.serviceOrder.customer = :customer and f.user <> :customer")
    List<Feedback> findAllAboutCustomer(User customer);

    @Query("select avg(f.rating) from Feedback f where f.serviceOrder.specialist = :specialist and f.user <> :specialist")
    Integer findAverageRatingAboutSpecialist(User specialist);

    @Query("select avg(f.rating) from Feedback f where f.serviceOrder.customer = :customer and f.user <> :customer")
    Integer findAverageRatingAboutCustomer(User customer);

    List<Feedback> findAllByUser(User user);

}
