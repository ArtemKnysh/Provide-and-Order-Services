package com.khai.edu.knysh.provide_and_order_services.controller;

import com.khai.edu.knysh.provide_and_order_services.entity.UserDTO;
import com.khai.edu.knysh.provide_and_order_services.exception.UserAlreadyExistException;
import com.khai.edu.knysh.provide_and_order_services.service.UserService;
import com.khai.edu.knysh.provide_and_order_services.validator.UserCompleteValidator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthenticationController {

    private final UserService userService;
    private final UserCompleteValidator userValidator;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationController(@Qualifier("userServiceImpl") UserService userService, UserCompleteValidator userValidator, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String login(Model model, String action) {
        if (action != null) {
            System.out.println("login :: action = " + action);
            model.addAttribute("action", action);
        }
        return "login";
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        UserDTO user = new UserDTO();
        model.addAttribute("userForm", user);
        return "registration";
    }

    @PostMapping("/registration")
    public String registerUserAccount(@ModelAttribute("userForm") UserDTO user, final BindingResult bindingResult,
                                      final Model model) {
        userValidator.validate(user, bindingResult);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (bindingResult.hasErrors()) {
            model.addAttribute("registrationForm", user);
            return "registration";
        }
        try {
            userService.save(user.toEntity());
            model.addAttribute("action", "registration-success");
            return "login";
        } catch (UserAlreadyExistException uaeEx) {
            bindingResult.rejectValue("email", "user.email", "An account for that email already exists.");
            model.addAttribute("registrationForm", user);
            return "registration";
        }
    }

}
