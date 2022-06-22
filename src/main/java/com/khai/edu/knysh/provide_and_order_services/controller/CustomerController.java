package com.khai.edu.knysh.provide_and_order_services.controller;

import com.khai.edu.knysh.provide_and_order_services.entity.AccountTransaction;
import com.khai.edu.knysh.provide_and_order_services.entity.User;
import com.khai.edu.knysh.provide_and_order_services.entity.UserDTO;
import com.khai.edu.knysh.provide_and_order_services.entity.UserRole;
import com.khai.edu.knysh.provide_and_order_services.exception.UserAlreadyExistException;
import com.khai.edu.knysh.provide_and_order_services.exception.UserNotFoundException;
import com.khai.edu.knysh.provide_and_order_services.service.AccountTransactionService;
import com.khai.edu.knysh.provide_and_order_services.service.FeedbackService;
import com.khai.edu.knysh.provide_and_order_services.service.ServiceOrderService;
import com.khai.edu.knysh.provide_and_order_services.service.UserService;
import com.khai.edu.knysh.provide_and_order_services.util.WebUtil;
import com.khai.edu.knysh.provide_and_order_services.validator.UserEmailValidator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    private final UserService userService;
    private final AccountTransactionService accountTransactionService;
    private final UserEmailValidator userValidator;
    private final WebUtil webUtil;
    private final FeedbackService feedbackService;
    private final ServiceOrderService serviceOrdersService;

    public CustomerController(@Qualifier("userServiceImpl") UserService userService, AccountTransactionService accountTransactionService, UserEmailValidator userValidator, WebUtil webUtil, FeedbackService feedbackService, ServiceOrderService serviceOrdersService) {
        this.userService = userService;
        this.accountTransactionService = accountTransactionService;
        this.userValidator = userValidator;
        this.webUtil = webUtil;
        this.feedbackService = feedbackService;
        this.serviceOrdersService = serviceOrdersService;
    }

    @GetMapping("/profile")
    public String profile(Principal principal, Model model) {
        User customer = webUtil.findUser(principal, userService);
        model.addAttribute("customer", customer);
        Double balance = accountTransactionService.findSumOfAmountByUser(customer);
        model.addAttribute("balance", balance);
        List<AccountTransaction> transactionList = accountTransactionService.findAllByUser(customer);
        model.addAttribute("transactionList", transactionList);
        model.addAttribute("averageRating", feedbackService.findAverageRatingAboutCustomer(customer));
        model.addAttribute("feedbacks", feedbackService.findAllAboutCustomer(customer));
        return "customer/profile";
    }


    @GetMapping("/public/profile/{id}")
    public String publicProfile(@PathVariable(name = "id") Long customerId, Principal principal, Model model) {
        User customer = userService.findCustomerById(customerId).orElseThrow(UserNotFoundException::new);
        model.addAttribute("customer", customer);
        model.addAttribute("averageRating", feedbackService.findAverageRatingAboutCustomer(customer));
        model.addAttribute("serviceOrdersCount", serviceOrdersService.countByCustomer(customer));
        model.addAttribute("feedbacks", feedbackService.findAllAboutCustomer(customer));
        if (principal != null) {
            User specialist = webUtil.findUser(principal, userService);
            if (specialist.getRole() == UserRole.ROLE_SPECIALIST) {
                model.addAttribute("serviceOrders", serviceOrdersService.findAllBySpecialistAndCustomer(specialist, customer));
            }
        }
        return "customer/public/profile";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") Long userId, String action, Model model) {
        Optional<User> customerOptional = userService.findById(userId);
        User user = customerOptional.orElseThrow(UserNotFoundException::new);
        UserDTO userDTO = new UserDTO(user);
        model.addAttribute("customer", userDTO);
        if (action != null) {
            model.addAttribute("action", action);
        }
        return "customer/edit";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute("customer") UserDTO userDTO, final BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model, Principal principal) {
        User customer = webUtil.findUser(principal, userService);
        userValidator.validate(userDTO, bindingResult);
        if (bindingResult.hasErrors() && !userDTO.getEmail().equals(customer.getEmail())) {
            model.addAttribute("customer", userDTO);
            return "customer/edit";
        }
        try {
            User user = userDTO.toEntity();
            userService.update(user);
            Collection<SimpleGrantedAuthority> nowAuthorities =
                    (Collection<SimpleGrantedAuthority>) SecurityContextHolder.getContext()
                            .getAuthentication()
                            .getAuthorities();
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), nowAuthorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            redirectAttributes.addAttribute("action", "edit-success");
            return "redirect:/customer/edit/" + user.getId();
        } catch (UserAlreadyExistException uaeEx) {
            bindingResult.rejectValue("email", "user.email", "An account for that email already exists.");
            model.addAttribute("customer", userDTO);
            return "customer/edit";
        }
    }


}
