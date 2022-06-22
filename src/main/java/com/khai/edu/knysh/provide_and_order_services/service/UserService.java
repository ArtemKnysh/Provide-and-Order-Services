package com.khai.edu.knysh.provide_and_order_services.service;

import com.khai.edu.knysh.provide_and_order_services.entity.User;
import com.khai.edu.knysh.provide_and_order_services.entity.UserRole;

import java.util.List;
import java.util.Optional;

public interface UserService extends GenericService<User> {

    void update(User user);

    void setRoleToUser(UserRole role, long userId);

    Optional<User> findByEmailAndPassword(String email, String password);

    Optional<User> findByEmail(String email);

    Optional<User> findCustomerById(long customerId);

    List<User> findAllByRole(UserRole role);

    List<User> findAllExcludeRoles(List<UserRole> roles);

    long countAllByRole(UserRole role);

    long countAllExcludeRoles(List<UserRole> roles);

}
