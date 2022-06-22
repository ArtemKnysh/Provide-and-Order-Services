package com.khai.edu.knysh.provide_and_order_services.controller;

import com.khai.edu.knysh.provide_and_order_services.entity.OfferServiceOrderDTO;
import com.khai.edu.knysh.provide_and_order_services.entity.ServiceOrder;
import com.khai.edu.knysh.provide_and_order_services.entity.ServiceOrderDTO;
import com.khai.edu.knysh.provide_and_order_services.entity.ServiceOrderStatus;
import com.khai.edu.knysh.provide_and_order_services.entity.SpecialistDTO;
import com.khai.edu.knysh.provide_and_order_services.entity.User;
import com.khai.edu.knysh.provide_and_order_services.exception.IncorrectServiceOrderStatusException;
import com.khai.edu.knysh.provide_and_order_services.exception.ServiceOrderNotFoundException;
import com.khai.edu.knysh.provide_and_order_services.service.FeedbackService;
import com.khai.edu.knysh.provide_and_order_services.service.OfferServiceOrderService;
import com.khai.edu.knysh.provide_and_order_services.service.ServiceOrderService;
import com.khai.edu.knysh.provide_and_order_services.service.SpecialistService;
import com.khai.edu.knysh.provide_and_order_services.service.UserService;
import com.khai.edu.knysh.provide_and_order_services.service.WorkCategoryService;
import com.khai.edu.knysh.provide_and_order_services.util.WebUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/service-order")
public class ServiceOrderController {

    private final ServiceOrderService serviceOrderService;
    private final UserService userService;
    private final SpecialistService specialistService;
    private final WorkCategoryService workCategoryService;
    private final OfferServiceOrderService offerServiceOrderService;
    private final WebUtil webUtil;
    private final FeedbackService feedbackService;

    ServiceOrderController(ServiceOrderService serviceOrderService, @Qualifier("userServiceImpl") UserService userService, SpecialistService specialistService, WorkCategoryService workCategoryService, OfferServiceOrderService offerServiceOrderService, WebUtil webUtil, FeedbackService feedbackService) {
        this.serviceOrderService = serviceOrderService;
        this.userService = userService;
        this.specialistService = specialistService;
        this.workCategoryService = workCategoryService;
        this.offerServiceOrderService = offerServiceOrderService;
        this.webUtil = webUtil;
        this.feedbackService = feedbackService;
    }

    @GetMapping("/list")
    public List<ServiceOrder> list() {
        return serviceOrderService.findAll();
    }

    @GetMapping("/customer/list")
    public String listByCustomer(Principal principal, Model model) {
        User customer = webUtil.findUser(principal, userService);
        model.addAttribute("orders", serviceOrderService.findAllNotCancelledByCustomer(customer));
        return "service-order/customer/list";
    }

    @GetMapping("/specialist/list")
    public String listBySpecialist(Principal principal, Model model) {
        User specialist = webUtil.findUser(principal, userService);
        model.addAttribute("orders", serviceOrderService.findAllBySpecialistAndStatusIn(specialist, ServiceOrderStatus.CANCELLED.getNextStatuses()));
        return "service-order/specialist/list";
    }

    @GetMapping("/specialist/offer/list")
    public String offerList(Principal principal, Model model) {
        User specialist = webUtil.findUser(principal, userService);
        model.addAttribute("receivedOffers", offerServiceOrderService.findAllNotAnswered(specialist).stream().map(OfferServiceOrderDTO::new).collect(Collectors.toList()));
        model.addAttribute("dismissedOffers", offerServiceOrderService.findAllWithAnswer(specialist, false).stream().map(OfferServiceOrderDTO::new).collect(Collectors.toList()));
        model.addAttribute("acceptedOffers", offerServiceOrderService.findAllWithAnswer(specialist, true).stream().map(OfferServiceOrderDTO::new).collect(Collectors.toList()));
        return "service-order/specialist/offer/list";
    }

    @GetMapping("/customer/details/{id}")
    public String getByCustomer(@PathVariable Long id, Model model) {
        Optional<ServiceOrder> serviceOrderOptional = serviceOrderService.findById(id);
        ServiceOrder order = serviceOrderOptional.orElseThrow(ServiceOrderNotFoundException::new);
        model.addAttribute("order", order);
        feedbackService.findByServiceOrderAndUser(order, order.getCustomer()).ifPresent(feedback -> model.addAttribute("customerFeedback", feedback));
        feedbackService.findByServiceOrderAndUser(order, order.getSpecialist()).ifPresent(feedback -> model.addAttribute("specialistFeedback", feedback));
        return "service-order/customer/details";
    }

    @GetMapping("/specialist/details/{id}")
    public String getBySpecialist(@PathVariable Long id, Model model) {
        Optional<ServiceOrder> serviceOrderOptional = serviceOrderService.findById(id);
        ServiceOrder order = serviceOrderOptional.orElseThrow(ServiceOrderNotFoundException::new);
        model.addAttribute("order", order);
        feedbackService.findByServiceOrderAndUser(order, order.getSpecialist()).ifPresent(feedback -> model.addAttribute("specialistFeedback", feedback));
        feedbackService.findByServiceOrderAndUser(order, order.getCustomer()).ifPresent(feedback -> model.addAttribute("customerFeedback", feedback));
        return "service-order/specialist/details";
    }

    @GetMapping("/customer/create")
    public String create(Principal principal, Model model) {
        User customer = webUtil.findUser(principal, userService);
        ServiceOrderDTO serviceOrder = new ServiceOrderDTO();
        serviceOrder.setCustomerId(customer.getId());
        model.addAttribute("order", serviceOrder);
        model.addAttribute("workCategories", workCategoryService.findAllParentCategories());
        return "service-order/customer/create";
    }

    @GetMapping("/customer/create/{select-work-category-id}")
    public String create(@PathVariable("select-work-category-id") Long selectWorkCategoryId, Principal principal, Model model) {
        String result = create(principal, model);
        model.addAttribute("selectWorkCategoryId", selectWorkCategoryId);
        return result;
    }

    @PostMapping("/customer/create")
    public String create(@ModelAttribute("order") ServiceOrderDTO serviceOrderDTO, RedirectAttributes redirectAttributes) {
        ServiceOrder serviceOrder = serviceOrderService.save(serviceOrderDTO.toEntity(workCategoryService, userService));
        redirectAttributes.addAttribute("action", "create-success");
        return "redirect:/service-order/customer/edit/" + serviceOrder.getId();
    }

    @PostMapping("/{id}")
    public void delete(@PathVariable Long id) {
        serviceOrderService.deleteById(id);
    }

    @GetMapping("/customer/edit/{id}")
    public String edit(@PathVariable(name = "id") Long serviceOrderId, String action, Model model) {
        Optional<ServiceOrder> serviceOrderOptional = serviceOrderService.findById(serviceOrderId);
        ServiceOrder serviceOrder = serviceOrderOptional.orElseThrow(ServiceOrderNotFoundException::new);
        if (serviceOrder.getStatus() != ServiceOrderStatus.CREATED) {
            throw new IncorrectServiceOrderStatusException();
        }
        model.addAttribute("order", new ServiceOrderDTO(serviceOrder));
        System.out.println("action = " + action);
        if (action != null) {
            model.addAttribute("action", action);
        }
        model.addAttribute("workCategories", workCategoryService.findAllParentCategories());
        return "service-order/customer/edit";
    }

    @PostMapping("/customer/edit")
    public String edit(@ModelAttribute("order") ServiceOrderDTO serviceOrderDTO, MultipartFile[] files, RedirectAttributes redirectAttributes) {
        ServiceOrder serviceOrder = serviceOrderService.save(serviceOrderDTO.toEntity(workCategoryService, userService));
        redirectAttributes.addAttribute("action", "edit-success");
        return "redirect:/service-order/customer/edit/" + serviceOrder.getId();
    }

    @GetMapping("/customer/find-specialist/{id}")
    public String findSpecialist(@PathVariable Long id, Model model, Principal principal) {
        ServiceOrder serviceOrder = serviceOrderService.findById(id).orElseThrow(ServiceOrderNotFoundException::new);
        if (serviceOrder.getSpecialist() != null) {
            throw new IllegalArgumentException();
        }
        Set<SpecialistDTO> allAcceptedOffers = specialistService.findAllAcceptedOffers(serviceOrder).stream()
                .map(specialist -> new SpecialistDTO(specialist, feedbackService.findAverageRatingAboutSpecialist(specialist)))
                .collect(Collectors.toSet());
        System.out.println("allAcceptedOffers = " + allAcceptedOffers);
        model.addAttribute("specialistsAcceptedOffer", allAcceptedOffers);

        Set<SpecialistDTO> allWithOffers = specialistService.findAllWithOffers(serviceOrder).stream()
                .map(specialist -> new SpecialistDTO(specialist, feedbackService.findAverageRatingAboutSpecialist(specialist)))
                .collect(Collectors.toSet());
        System.out.println("allWithOffers = " + allWithOffers);
        model.addAttribute("specialistsNotAcceptedOffer", allWithOffers);

        Set<SpecialistDTO> allWithOutOffers = specialistService.findAllWithOutOffers(id).stream()
                .map(specialist -> new SpecialistDTO(specialist, feedbackService.findAverageRatingAboutSpecialist(specialist)))
                .collect(Collectors.toSet());
        System.out.println("allWithOutOffers = " + allWithOutOffers);
        model.addAttribute("specialistsWithOutOffer", allWithOutOffers);

        model.addAttribute("selectedOrder", serviceOrder);
        User customer = webUtil.findUser(principal, userService);
        model.addAttribute("orders", serviceOrderService.findAllByCustomerAndSpecialistIsNull(customer));
        return "service-order/customer/find-specialist";
    }

    @PostMapping("/customer/specialist/set")
    public String setSpecialist(@RequestParam Long specialistId, @RequestParam Long serviceOrderId) {
        serviceOrderService.setSpecialistToServiceOrder(userService.findById(specialistId).get(), serviceOrderId);
        return "redirect:/service-order/customer/details/" + serviceOrderId;
    }

    @PostMapping("/cancel")
    public void cancel(Long serviceOrderId) {
        serviceOrderService.cancelServiceOrder(serviceOrderId);
    }

    @PostMapping("/edit/description")
    public void editDescription(@RequestParam String description, @RequestParam Long serviceOrderId) {
        serviceOrderService.setDescriptionToServiceOrder(description, serviceOrderId);
    }

    @PostMapping("/edit/cost")
    public void editCost(@RequestParam Double cost, @RequestParam Long serviceOrderId) {
        serviceOrderService.setCostToServiceOrder(cost, serviceOrderId);
    }

    @PostMapping("/edit/specialist")
    public void editSpecialist(@RequestParam User specialist, @RequestParam Long serviceOrderId) {
        serviceOrderService.setSpecialistToServiceOrder(specialist, serviceOrderId);
    }

    @PostMapping("/remove/specialist")
    public void removeSpecialist(@RequestParam Long serviceOrderId) {
        serviceOrderService.removeSpecialistFromServiceOrder(serviceOrderId);
    }

    @PostMapping("/edit/status")
    public void editStatus(@RequestParam ServiceOrderStatus status, @RequestParam Long serviceOrderId) {
        serviceOrderService.setStatusToServiceOrder(status, serviceOrderId);
    }

}
