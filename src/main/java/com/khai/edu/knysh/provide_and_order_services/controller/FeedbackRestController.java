package com.khai.edu.knysh.provide_and_order_services.controller;

import com.khai.edu.knysh.provide_and_order_services.entity.Feedback;
import com.khai.edu.knysh.provide_and_order_services.entity.Response;
import com.khai.edu.knysh.provide_and_order_services.entity.ServiceOrder;
import com.khai.edu.knysh.provide_and_order_services.exception.ServiceOrderNotFoundException;
import com.khai.edu.knysh.provide_and_order_services.service.FeedbackService;
import com.khai.edu.knysh.provide_and_order_services.service.ServiceOrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/feedback")
public class FeedbackRestController {

    private final FeedbackService feedbackService;
    private final ServiceOrderService serviceOrderService;

    public FeedbackRestController(FeedbackService feedbackService, ServiceOrderService serviceOrderService) {
        this.feedbackService = feedbackService;
        this.serviceOrderService = serviceOrderService;
    }

    @PostMapping("/customer/edit")
    public Response editByCustomer(@RequestParam Long serviceOrderId, @RequestParam String text, @RequestParam Integer rating) {
        ServiceOrder serviceOrder = serviceOrderService.findById(serviceOrderId).orElseThrow(ServiceOrderNotFoundException::new);
        Optional<Feedback> optionalFeedback = feedbackService.findByServiceOrderAndUser(serviceOrder, serviceOrder.getCustomer());
        Feedback feedback = optionalFeedback.orElseGet(() -> new Feedback(serviceOrder, serviceOrder.getCustomer()));
        feedback.setText(text);
        feedback.setRating(rating);
        feedbackService.save(feedback);
        return new Response(null, "Відгук та оцінку успішно збережено!");
    }

    @PostMapping("/specialist/edit")
    public Response editBySpecialist(@RequestParam Long serviceOrderId, @RequestParam String text, @RequestParam Integer rating) {
        ServiceOrder serviceOrder = serviceOrderService.findById(serviceOrderId).orElseThrow(ServiceOrderNotFoundException::new);
        Optional<Feedback> optionalFeedback = feedbackService.findByServiceOrderAndUser(serviceOrder, serviceOrder.getSpecialist());
        Feedback feedback = optionalFeedback.orElseGet(() -> new Feedback(serviceOrder, serviceOrder.getSpecialist()));
        feedback.setText(text);
        feedback.setRating(rating);
        feedbackService.save(feedback);
        return new Response(null, "Відгук та оцінку успішно збережено!");
    }

}
