package com.example.belle.data.controller;

import com.example.belle.data.model.Product;
import com.example.belle.data.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping({"/", "/home"})
public class HomeController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public String getHomePage(Model model) {
        // Lấy danh sách 6 sản phẩm có lượt xem cao nhất (bán chạy)
        List<Product> topProducts = productService.getTop6ByPrice();

        // Lấy danh sách 8 sản phẩm mới nhất, sắp xếp theo thời gian tạo
        List<Product> newProducts = productService.getTop8ByCreatedAt();

        // Đưa danh sách vào model
        model.addAttribute("topProducts", topProducts);
        model.addAttribute("newProducts", newProducts);
        model.addAttribute("pageTitle", "Home - Belle Fashion");

        return "home";
    }
}
