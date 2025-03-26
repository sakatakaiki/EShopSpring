package com.example.belle.data.controller;

import com.example.belle.data.model.Category;
import com.example.belle.data.model.Product;
import com.example.belle.data.service.CategoryService;
import com.example.belle.data.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;


import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @GetMapping("/{id}")
    public String getCategoryById(
            @PathVariable Long id,
            @RequestParam(name = "property", defaultValue = "name") String property,
            @RequestParam(name = "order", defaultValue = "asc") String order,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            Model model) {

        Optional<Category> category = categoryService.getCategoryById(id);
        if (category.isPresent()) {
            Page<Product> products = productService.getProductsByCategorySorted(id, property, order, page, size);
            List<Product> topProducts = productService.getTopProducts();
            model.addAttribute("category", category.get());
            model.addAttribute("products", products); // Đảm bảo products là Page<Product>
            model.addAttribute("topProducts", topProducts);
            model.addAttribute("property", property);
            model.addAttribute("order", order);
        }
        return "categories";
    }

}
