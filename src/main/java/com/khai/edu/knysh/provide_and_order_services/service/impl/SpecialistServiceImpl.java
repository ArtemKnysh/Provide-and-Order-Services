package com.khai.edu.knysh.provide_and_order_services.service.impl;

import com.khai.edu.knysh.provide_and_order_services.entity.ServiceOrder;
import com.khai.edu.knysh.provide_and_order_services.entity.User;
import com.khai.edu.knysh.provide_and_order_services.entity.UserRole;
import com.khai.edu.knysh.provide_and_order_services.repository.SpecialistRepository;
import com.khai.edu.knysh.provide_and_order_services.service.SpecialistService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class SpecialistServiceImpl extends UserServiceImpl implements SpecialistService {

    private final SpecialistRepository repository;

    protected SpecialistServiceImpl(SpecialistRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public Set<User> findAllByWorkCategoryId(long workCategoryId) {
        return repository.findAllByRoleAndWorkCategoryId(UserRole.ROLE_SPECIALIST, workCategoryId);
    }

    @Override
    public User findSpecialistById(long specialistId) {
        return repository.findByIdAndRole(specialistId, UserRole.ROLE_SPECIALIST);
    }

    @Override
    public Set<User> findAllWithOffers(ServiceOrder serviceOrder) {
        return repository.findAllWithOffers(UserRole.ROLE_SPECIALIST, serviceOrder);
    }

    @Override
    public Set<User> findAllAcceptedOffers(ServiceOrder serviceOrder) {
        return repository.findAllByRoleAndOffers_ServiceOrderAndOffers_SpecialistAnswer(UserRole.ROLE_SPECIALIST, serviceOrder, true);
    }

    @Override
    public Set<User> findAllWithOutOffers(long serviceOrderId) {
        List<User> specialists = findAllByRole(UserRole.ROLE_SPECIALIST);
        return specialists.stream().filter(isUserHasNoOffers(serviceOrderId)).collect(Collectors.toSet());
    }

    private Predicate<User> isUserHasNoOffers(long serviceOrderId) {
        return specialist -> specialist.getOffers().isEmpty() || specialist.getOffers().stream()
                .filter(offer -> offer.getServiceOrder().getId() == serviceOrderId).findAny().isEmpty();
    }

}
