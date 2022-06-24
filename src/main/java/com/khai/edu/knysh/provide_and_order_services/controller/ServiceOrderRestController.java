package com.khai.edu.knysh.provide_and_order_services.controller;

import com.khai.edu.knysh.provide_and_order_services.entity.OfferServiceOrder;
import com.khai.edu.knysh.provide_and_order_services.entity.OfferServiceOrderDTO;
import com.khai.edu.knysh.provide_and_order_services.entity.Response;
import com.khai.edu.knysh.provide_and_order_services.entity.ServiceOrder;
import com.khai.edu.knysh.provide_and_order_services.entity.ServiceOrderStatus;
import com.khai.edu.knysh.provide_and_order_services.entity.SpecialistDTO;
import com.khai.edu.knysh.provide_and_order_services.entity.User;
import com.khai.edu.knysh.provide_and_order_services.exception.LowUserBalanceException;
import com.khai.edu.knysh.provide_and_order_services.exception.ServiceOrderNotFoundException;
import com.khai.edu.knysh.provide_and_order_services.service.AccountTransactionService;
import com.khai.edu.knysh.provide_and_order_services.service.FeedbackService;
import com.khai.edu.knysh.provide_and_order_services.service.OfferServiceOrderService;
import com.khai.edu.knysh.provide_and_order_services.service.ServiceOrderService;
import com.khai.edu.knysh.provide_and_order_services.service.SpecialistService;
import com.khai.edu.knysh.provide_and_order_services.service.UserService;
import com.khai.edu.knysh.provide_and_order_services.util.WebUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/service-order")
public class ServiceOrderRestController {

    private final SpecialistService specialistService;
    private final ServiceOrderService serviceOrderService;
    private final OfferServiceOrderService offerServiceOrderService;
    private final UserService userService;
    private final AccountTransactionService accountTransactionService;
    private final WebUtil webUtil;
    private final FeedbackService feedbackService;

    public ServiceOrderRestController(SpecialistService specialistService, ServiceOrderService serviceOrderService, OfferServiceOrderService offerServiceOrderService, @Qualifier("userServiceImpl") UserService userService, AccountTransactionService accountTransactionService, WebUtil webUtil, FeedbackService feedbackService) {
        this.specialistService = specialistService;
        this.serviceOrderService = serviceOrderService;
        this.offerServiceOrderService = offerServiceOrderService;
        this.userService = userService;
        this.accountTransactionService = accountTransactionService;
        this.webUtil = webUtil;
        this.feedbackService = feedbackService;
    }

    @PostMapping("/customer/specialist/offer/remove")
    public SpecialistDTO removeOffer(@RequestParam Long specialistId, @RequestParam Long serviceOrderId) {
        OfferServiceOrder offer = offerServiceOrderService.findBySpecialistIdAndServiceOrderId(specialistId, serviceOrderId);
        offerServiceOrderService.delete(offer);
        User specialist = specialistService.findSpecialistById(specialistId);
        return new SpecialistDTO(specialist, feedbackService.findAverageRatingAboutSpecialist(specialist));
    }

    @PostMapping("/customer/specialist/offer")
    public SpecialistDTO addOffer(@RequestParam Long specialistId, @RequestParam Long serviceOrderId) {
        ServiceOrder serviceOrder = serviceOrderService.findById(serviceOrderId).orElseThrow(ServiceOrderNotFoundException::new);
        User specialist = specialistService.findSpecialistById(specialistId);
        OfferServiceOrder offer = new OfferServiceOrder();
        offer.setSpecialist(specialist);
        offer.setServiceOrder(serviceOrder);
        offerServiceOrderService.save(offer);
        return new SpecialistDTO(specialist, feedbackService.findAverageRatingAboutSpecialist(specialist));
    }

    @PostMapping("/specialist/offer/dismiss")
    public OfferServiceOrderDTO dismissOffer(@RequestParam Long specialistId, @RequestParam Long serviceOrderId) {
        OfferServiceOrder offer = offerServiceOrderService.findBySpecialistIdAndServiceOrderId(specialistId, serviceOrderId);
        offer.setSpecialistAnswer(false);
        offerServiceOrderService.save(offer);
        return new OfferServiceOrderDTO(offer);
    }

    @PostMapping("/specialist/offer/accept")
    public OfferServiceOrderDTO acceptOffer(@RequestParam Long specialistId, @RequestParam Long serviceOrderId) {
        OfferServiceOrder offer = offerServiceOrderService.findBySpecialistIdAndServiceOrderId(specialistId, serviceOrderId);
        offer.setSpecialistAnswer(true);
        offerServiceOrderService.save(offer);
        return new OfferServiceOrderDTO(offer);
    }

    @PostMapping("/specialist/complete")
    public Response completeBySpecialist(@RequestParam Long serviceOrderId) {
        serviceOrderService.setStatusToServiceOrder(ServiceOrderStatus.WAIT_FOR_CUSTOMER_APPROVE, serviceOrderId);
        return new Response(ServiceOrderStatus.WAIT_FOR_CUSTOMER_APPROVE.toString(),
                "Service Order completing successfully confirmed! Please wait for approving by customer...");
    }

    @PostMapping("/customer/approve")
    public Response approveByCustomer(@RequestParam Long serviceOrderId) {
        serviceOrderService.setStatusToServiceOrder(ServiceOrderStatus.WAIT_FOR_PAYMENT, serviceOrderId);
        return new Response(ServiceOrderStatus.WAIT_FOR_PAYMENT.toString(),
                "Service Order completing successfully approved!");
    }

    @PostMapping("/customer/extend")
    public Response extendByCustomer(@RequestParam Long serviceOrderId) {
        serviceOrderService.setStatusToServiceOrder(ServiceOrderStatus.IN_WORK, serviceOrderId);
        return new Response(ServiceOrderStatus.IN_WORK.toString(),
                "Service Order sent to refinement...");
    }

    @PostMapping("/customer/pay")
    public Response payByCustomer(@RequestParam Long serviceOrderId, Principal principal) {
        User specialist = webUtil.findUser(principal, userService);
        try {
            accountTransactionService.payForServiceOrder(specialist.getId(), serviceOrderId);
            return new Response(ServiceOrderStatus.COMPLETED.toString(),
                    "Service Order was successfully paid!");
        } catch (LowUserBalanceException ex) {
            return new Response(ServiceOrderStatus.WAIT_FOR_PAYMENT.toString(),
                    "Not enough money on the account to complete payment...", true);
        }
    }

}
