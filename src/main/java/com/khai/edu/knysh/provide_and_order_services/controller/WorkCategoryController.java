package com.khai.edu.knysh.provide_and_order_services.controller;

import com.khai.edu.knysh.provide_and_order_services.service.WorkCategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/work-category")
public class WorkCategoryController {

    private final WorkCategoryService service;

    public WorkCategoryController(WorkCategoryService service) {
        this.service = service;
    }

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("parentCategories", service.findAllParentCategories());
        return "work-category/list";
    }

}
