package com.example.belle.data.controller;

import com.example.belle.data.model.Category;
import com.example.belle.data.service.CategoryService;
import com.example.belle.data.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @GetMapping("/categories/{id}")
    public String getCategoryById(@PathVariable Long id, Model model) {
        categoryService.getCategoryById(id).ifPresent(category -> {
            model.addAttribute("category", category);
            model.addAttribute("topProducts", productService.getTopProducts());
        });
        return "categories";
    }
}