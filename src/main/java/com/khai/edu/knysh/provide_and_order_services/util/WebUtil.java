package com.khai.edu.knysh.provide_and_order_services.util;

import com.khai.edu.knysh.provide_and_order_services.entity.User;
import com.khai.edu.knysh.provide_and_order_services.service.UserService;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class WebUtil {

    public User findUser(Principal principal, UserService userService) {
        String username = principal.getName();
        return userService.findByEmail(username).orElseThrow(IllegalArgumentException::new);
    }

}
