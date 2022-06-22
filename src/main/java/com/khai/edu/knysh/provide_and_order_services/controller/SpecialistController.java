package com.khai.edu.knysh.provide_and_order_services.controller;

import com.khai.edu.knysh.provide_and_order_services.entity.AccountTransaction;
import com.khai.edu.knysh.provide_and_order_services.entity.ServiceOrderStatus;
import com.khai.edu.knysh.provide_and_order_services.entity.SpecialistDTO;
import com.khai.edu.knysh.provide_and_order_services.entity.User;
import com.khai.edu.knysh.provide_and_order_services.entity.UserDTO;
import com.khai.edu.knysh.provide_and_order_services.entity.UserRole;
import com.khai.edu.knysh.provide_and_order_services.entity.WorkCategory;
import com.khai.edu.knysh.provide_and_order_services.exception.UserAlreadyExistException;
import com.khai.edu.knysh.provide_and_order_services.exception.UserNotFoundException;
import com.khai.edu.knysh.provide_and_order_services.exception.WorkCategoryNotFoundException;
import com.khai.edu.knysh.provide_and_order_services.service.AccountTransactionService;
import com.khai.edu.knysh.provide_and_order_services.service.FeedbackService;
import com.khai.edu.knysh.provide_and_order_services.service.ServiceOrderService;
import com.khai.edu.knysh.provide_and_order_services.service.SpecialistService;
import com.khai.edu.knysh.provide_and_order_services.service.WorkCategoryService;
import com.khai.edu.knysh.provide_and_order_services.util.WebUtil;
import com.khai.edu.knysh.provide_and_order_services.validator.UserEmailValidator;
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
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/specialist")
public class SpecialistController {

    private final SpecialistService specialistService;
    private final AccountTransactionService accountTransactionService;
    private final WorkCategoryService workCategoryService;
    private final UserEmailValidator userValidator;
    private final WebUtil webUtil;
    private final FeedbackService feedbackService;
    private final ServiceOrderService serviceOrdersService;

    public SpecialistController(SpecialistService specialistService, AccountTransactionService accountTransactionService, WorkCategoryService workCategoryService, UserEmailValidator userValidator, WebUtil webUtil, FeedbackService feedbackService, ServiceOrderService serviceOrdersService) {
        this.specialistService = specialistService;
        this.accountTransactionService = accountTransactionService;
        this.workCategoryService = workCategoryService;
        this.userValidator = userValidator;
        this.webUtil = webUtil;
        this.feedbackService = feedbackService;
        this.serviceOrdersService = serviceOrdersService;
    }

    @GetMapping("/list/work-category/{id}")
    public String listByWorkCategory(@PathVariable(name = "id") long workCategoryId, Model model) {
        WorkCategory workCategory = workCategoryService.findById(workCategoryId).orElseThrow(WorkCategoryNotFoundException::new);
        model.addAttribute("workCategory", workCategory);
        Set<SpecialistDTO> allByWorkCategoryId = specialistService.findAllByWorkCategoryId(workCategoryId).stream()
                .map(specialist -> new SpecialistDTO(specialist, feedbackService.findAverageRatingAboutSpecialist(specialist)))
                .collect(Collectors.toSet());
        model.addAttribute("specialists", allByWorkCategoryId);
        return "specialist/work-category/list";
    }

    @GetMapping("/profile")
    public String profile(Principal principal, Model model) {
        User specialist = webUtil.findUser(principal, specialistService);
        model.addAttribute("specialist", specialist);
        Double balance = accountTransactionService.findSumOfAmountByUser(specialist);
        model.addAttribute("balance", balance);
        List<AccountTransaction> transactionList = accountTransactionService.findAllByUser(specialist);
        model.addAttribute("transactionList", transactionList);
        model.addAttribute("specialistWorkCategories", workCategoryService.findAllBySpecialistId(specialist.getId()));
        model.addAttribute("allWorkCategories", workCategoryService.findAll());
        model.addAttribute("averageRating", feedbackService.findAverageRatingAboutSpecialist(specialist));
        model.addAttribute("feedbacks", feedbackService.findAllAboutSpecialist(specialist));
        return "specialist/profile";
    }

    @GetMapping("/public/profile/{id}")
    public String publicProfile(@PathVariable(name = "id") Long specialistId, Principal principal, Model model) {
        User specialist = specialistService.findSpecialistById(specialistId);
        model.addAttribute("specialist", specialist);
        model.addAttribute("specialistWorkCategories", workCategoryService.findAllBySpecialistId(specialist.getId()));
        model.addAttribute("averageRating", feedbackService.findAverageRatingAboutSpecialist(specialist));
        model.addAttribute("serviceOrdersCount", serviceOrdersService.countBySpecialistAndStatusIn(specialist, ServiceOrderStatus.CANCELLED.getNextStatuses()));
        model.addAttribute("feedbacks", feedbackService.findAllAboutSpecialist(specialist));
        if (principal != null) {
            User user = webUtil.findUser(principal, specialistService);
            if (user.getRole() == UserRole.ROLE_CUSTOMER) {
                model.addAttribute("serviceOrders", serviceOrdersService.findAllBySpecialistAndCustomer(specialist, user));
            }
        }
        return "specialist/public/profile";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") Long userId, String action, Model model) {
        Optional<User> specialistOptional = specialistService.findById(userId);
        User user = specialistOptional.orElseThrow(UserNotFoundException::new);
        UserDTO userDTO = new UserDTO(user);
        model.addAttribute("specialist", userDTO);
        if (action != null) {
            model.addAttribute("action", action);
        }
        return "specialist/edit";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute("specialist") UserDTO userDTO, final BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        userValidator.validate(userDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("specialist", userDTO);
            return "specialist/edit";
        }
        try {
            User user = userDTO.toEntity();
            specialistService.update(user);
            Collection<SimpleGrantedAuthority> nowAuthorities =
                    (Collection<SimpleGrantedAuthority>) SecurityContextHolder.getContext()
                            .getAuthentication()
                            .getAuthorities();
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), nowAuthorities);

            SecurityContextHolder.getContext().setAuthentication(authentication);
            redirectAttributes.addAttribute("action", "edit-success");
            return "redirect:/specialist/edit/" + user.getId();
        } catch (UserAlreadyExistException uaeEx) {
            bindingResult.rejectValue("email", "user.email", "An account for that email already exists.");
            model.addAttribute("specialist", userDTO);
            return "specialist/edit";
        }
    }

}
