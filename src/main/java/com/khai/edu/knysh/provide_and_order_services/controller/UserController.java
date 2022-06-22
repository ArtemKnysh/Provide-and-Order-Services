package com.khai.edu.knysh.provide_and_order_services.controller;

import com.khai.edu.knysh.provide_and_order_services.entity.User;
import com.khai.edu.knysh.provide_and_order_services.entity.UserRole;
import com.khai.edu.knysh.provide_and_order_services.exception.UserNotFoundException;
import com.khai.edu.knysh.provide_and_order_services.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(@Qualifier("userServiceImpl") UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("userRoles", UserRole.values());
        return "user/list";
    }

    @GetMapping("/{id}")
    public User get(@PathVariable Long id) {
        return userService.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return userService.save(user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.deleteById(id);
    }

    @GetMapping("/specialist/list")
    public List<User> specialistList() {
        return userService.findAllByRole(UserRole.ROLE_SPECIALIST);
    }

    @GetMapping("/customer/list")
    public List<User> customerList() {
        return userService.findAllByRole(UserRole.ROLE_CUSTOMER);
    }

    @PostMapping("/set-role")
    public void setRole(@RequestParam UserRole role, @RequestParam User user) {
        userService.setRoleToUser(role, user.getId());
    }

    @PostMapping("/edit")
    public void edit(@RequestParam User user) {
        userService.save(user);
    }

    @GetMapping("/edit/{id}")
    public User edit(@PathVariable(name = "id") Long userId) {
        Optional<User> userOptional = userService.findById(userId);
        return userOptional.orElseThrow(UserNotFoundException::new);
    }

}
