package com.khai.edu.knysh.provide_and_order_services.service.impl;

import com.khai.edu.knysh.provide_and_order_services.entity.User;
import com.khai.edu.knysh.provide_and_order_services.entity.UserRole;
import com.khai.edu.knysh.provide_and_order_services.repository.UserRepository;
import com.khai.edu.knysh.provide_and_order_services.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl extends AbstractService<User> implements UserService {

    private final UserRepository userRepository;

    protected UserServiceImpl(@Qualifier("userRepository") UserRepository repository) {
        super(repository);
        userRepository = repository;
    }

    @Override
    public void update(User user) {
        userRepository.update(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhoneNumber(), user.getId());
    }

    @Override
    public void setRoleToUser(UserRole role, long userId) {
        userRepository.setRoleToUser(role, userId);
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        return Optional.ofNullable(userRepository.findByEmailAndPassword(email, password));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        User byEmail = userRepository.findByEmail(email);
        return Optional.ofNullable(byEmail);
    }

    @Override
    public Optional<User> findCustomerById(long customerId) {
        Optional<User> optionalUser = userRepository.findById(customerId);
        if (optionalUser.isEmpty()) {
            return Optional.empty();
        }
        User user = optionalUser.get();
        if (user.getRole() == UserRole.ROLE_CUSTOMER) {
            return Optional.of(user);
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAllByRole(UserRole role) {
        return userRepository.findAllByRole(role);
    }

    @Override
    public List<User> findAllExcludeRoles(List<UserRole> roles) {
        return userRepository.findAllByRoleIsNotIn(roles);
    }

    @Override
    public long countAllByRole(UserRole role) {
        return userRepository.countAllByRole(role);
    }

    @Override
    public long countAllExcludeRoles(List<UserRole> roles) {
        return userRepository.countAllByRoleIsNotIn(roles);
    }

}
