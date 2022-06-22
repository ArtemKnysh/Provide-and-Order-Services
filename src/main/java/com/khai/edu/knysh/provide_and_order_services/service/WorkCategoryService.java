package com.khai.edu.knysh.provide_and_order_services.service;

import com.khai.edu.knysh.provide_and_order_services.entity.WorkCategory;

import java.util.List;

public interface WorkCategoryService extends GenericService<WorkCategory> {

    List<WorkCategory> findAllParentCategories();

    List<WorkCategory> findAllBySpecialistId(Long specialistId);
}
