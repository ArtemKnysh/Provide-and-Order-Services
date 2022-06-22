package com.khai.edu.knysh.provide_and_order_services.controller;

import com.khai.edu.knysh.provide_and_order_services.entity.User;
import com.khai.edu.knysh.provide_and_order_services.entity.WorkCategory;
import com.khai.edu.knysh.provide_and_order_services.entity.WorkCategoryChanged;
import com.khai.edu.knysh.provide_and_order_services.exception.WorkCategoryNotFoundException;
import com.khai.edu.knysh.provide_and_order_services.service.SpecialistService;
import com.khai.edu.knysh.provide_and_order_services.service.WorkCategoryService;
import com.khai.edu.knysh.provide_and_order_services.validator.UserEmailValidator;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/specialist")
public class SpecialistRestController {

    private final SpecialistService specialistService;
    private final WorkCategoryService workCategoryService;

    public SpecialistRestController(SpecialistService specialistService, WorkCategoryService workCategoryService, UserEmailValidator userValidator) {
        this.specialistService = specialistService;
        this.workCategoryService = workCategoryService;
    }

    @PostMapping("/work-category/add")
    public WorkCategoryChanged addWorkCategory(@RequestParam Long specialistId, @RequestParam Long workCategoryId) {
        WorkCategory workCategory = workCategoryService.findById(workCategoryId).orElseThrow(WorkCategoryNotFoundException::new);
        User specialist = specialistService.findSpecialistById(specialistId);
        if (!workCategory.getSpecialists().add(specialist)) {
            throw new IllegalArgumentException();
        }
        workCategoryService.save(workCategory);
        return new WorkCategoryChanged(workCategory, specialist);
    }

    @PostMapping("/work-category/remove")
    public WorkCategoryChanged removeWorkCategory(@RequestParam Long specialistId, @RequestParam Long workCategoryId) {
        WorkCategory workCategory = workCategoryService.findById(workCategoryId).orElseThrow(WorkCategoryNotFoundException::new);
        User specialist = specialistService.findSpecialistById(specialistId);
        workCategory.getSpecialists().remove(specialist);
        workCategoryService.save(workCategory);
        return new WorkCategoryChanged(workCategory, specialist);
    }

}
