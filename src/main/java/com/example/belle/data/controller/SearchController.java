package com.example.belle.data.controller;

import com.example.belle.data.model.Product;
import com.example.belle.data.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;

@Controller
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public String search(@RequestParam String q,
                         @RequestParam(defaultValue = "name") String property,
                         @RequestParam(defaultValue = "asc") String order,
                         @RequestParam(name = "page", defaultValue = "0") int page,
                         @RequestParam(name = "size", defaultValue = "8") int size,
                         Model model) {
        // Tìm kiếm sản phẩm theo từ khóa và các tham số sắp xếp, với phân trang
        Page<Product> products = productService.searchProducts(q, property, order, page, size);

        // Thêm các giá trị vào model để hiển thị trên view
        model.addAttribute("query", q);
        model.addAttribute("products", products);  // Đảm bảo products là Page<Product>
        model.addAttribute("property", property);
        model.addAttribute("order", order);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());

        return "search";
    }
}
