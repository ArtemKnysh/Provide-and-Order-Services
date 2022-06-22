package com.khai.edu.knysh.provide_and_order_services.controller;

import com.khai.edu.knysh.provide_and_order_services.entity.User;
import com.khai.edu.knysh.provide_and_order_services.service.FeedbackService;
import com.khai.edu.knysh.provide_and_order_services.service.UserService;
import com.khai.edu.knysh.provide_and_order_services.util.WebUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final WebUtil webUtil;
    private final UserService userService;

    public FeedbackController(FeedbackService feedbackService, WebUtil webUtil, @Qualifier("userServiceImpl") UserService userService) {
        this.feedbackService = feedbackService;
        this.webUtil = webUtil;
        this.userService = userService;
    }

    @GetMapping("/specialist/list")
    public String listBySpecialist(Principal principal, Model model) {
        User specialist = webUtil.findUser(principal, userService);
        model.addAttribute("feedbacks", feedbackService.findAllCreatedBySpecialist(specialist));
        return "feedback/specialist/list";
    }

    @GetMapping("/customer/list")
    public String listByCustomer(Principal principal, Model model) {
        User customer = webUtil.findUser(principal, userService);
        model.addAttribute("feedbacks", feedbackService.findAllCreatedByCustomer(customer));
        return "feedback/customer/list";
    }

}
