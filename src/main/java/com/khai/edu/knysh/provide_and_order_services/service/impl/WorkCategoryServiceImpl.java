package com.khai.edu.knysh.provide_and_order_services.service.impl;

import com.khai.edu.knysh.provide_and_order_services.entity.WorkCategory;
import com.khai.edu.knysh.provide_and_order_services.repository.WorkCategoryRepository;
import com.khai.edu.knysh.provide_and_order_services.service.WorkCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkCategoryServiceImpl extends AbstractService<WorkCategory> implements WorkCategoryService {

    private final WorkCategoryRepository repository;

    protected WorkCategoryServiceImpl(WorkCategoryRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public List<WorkCategory> findAllParentCategories() {
        return repository.findAllByParentIsNull();
    }

    @Override
    public List<WorkCategory> findAllBySpecialistId(Long specialistId) {
        return repository.findAllBySpecialists_Id(specialistId);
    }
}
