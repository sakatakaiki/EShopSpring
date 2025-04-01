package com.example.belle.data.controller;

import com.example.belle.data.model.Product;
import com.example.belle.data.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;


    // Hiển thị chi tiết sản phẩm trên trang products.html
    @GetMapping("/product/{id}")
    public String getProductDetail(@PathVariable Long id, Model model) {
        Optional<Product> productOptional = productService.getProductById(id);

        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            model.addAttribute("product", product);

            // Lấy sản phẩm liên quan (cùng danh mục)
            List<Product> relatedProducts = productService.getProductsByCategoryName(product.getCategory().getName());
            relatedProducts.removeIf(p -> p.getId().equals(product.getId()));
            model.addAttribute("relatedProducts", relatedProducts);

            return "products"; // Trả về giao diện chi tiết sản phẩm
        } else {
            return "redirect:/"; // Chuyển hướng về trang chủ nếu không tìm thấy sản phẩm
        }
    }

}