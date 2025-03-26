package com.example.belle.data.controller;

import com.example.belle.data.model.Category;
import com.example.belle.data.service.CategoryService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;
import java.util.List;

@ControllerAdvice
public class GlobalController {

    private final CategoryService categoryService;

    public GlobalController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @ModelAttribute
    public void addGlobalAttributes(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        List<Category> topCategories = categoryService.getTopCategories();

        model.addAttribute("categories", categories);
        model.addAttribute("topCategories", topCategories);
    }
}
