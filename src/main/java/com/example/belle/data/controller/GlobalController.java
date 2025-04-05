package com.example.belle.data.controller;

import com.example.belle.data.model.Category;
import com.example.belle.data.model.OrderItem;
import com.example.belle.data.service.CategoryService;
import com.example.belle.data.service.OrderItemService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;
import java.util.List;
import com.example.belle.data.model.User;

@ControllerAdvice
public class GlobalController {

    private final CategoryService categoryService;

    @Autowired
    private OrderItemService orderItemService;

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

    @ModelAttribute("cartItems")
    public List<OrderItem> getCartItems(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return List.of();
        }
        return orderItemService.getCartItems(user.getId());
    }

    @ModelAttribute("cartCount")
    public int getCartCount(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return 0;
        }
        return orderItemService.getCartItems(user.getId()).size();
    }

    @ModelAttribute("cartTotal")
    public double getCartTotal(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return 0.0;
        }
        return orderItemService.getCartItems(user.getId()).stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

}