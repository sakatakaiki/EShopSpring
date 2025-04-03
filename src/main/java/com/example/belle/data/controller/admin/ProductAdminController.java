package com.example.belle.data.controller.admin;

import com.example.belle.data.model.Category;
import com.example.belle.data.model.Product;
import com.example.belle.data.service.ProductService;
import com.example.belle.data.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin/products")
public class ProductAdminController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    // List all products with pagination
    @GetMapping
    public String listProducts(@RequestParam(defaultValue = "0") int page, Model model) {
        Pageable pageable = PageRequest.of(page, 10);  // 10 items per page
        Page<Product> productPage = productService.getAllProductsForAdmin(pageable);
        model.addAttribute("products", productPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        return "admin/products/index";  // Thymeleaf template
    }


    // Create a new product
    @GetMapping("/create")
    public String createProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/products/create";  // Thymeleaf template
    }

    @PostMapping("/store")
    public String storeProduct(@RequestParam("name") String name,
                               @RequestParam("description") String description,
                               @RequestParam("thumbnail") String thumbnail,
                               @RequestParam("price") Double price,
                               @RequestParam("quantity") Integer quantity,
                               @RequestParam("category_id") Long categoryId) {
        // Tạo mới một đối tượng Product
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setThumbnail(thumbnail);
        product.setPrice(price);
        product.setQuantity(quantity);

        // Lấy category từ ID và gán vào product
        Category category = categoryService.getCategoryById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + categoryId));
        product.setCategory(category);

        // Lưu sản phẩm mới vào database
        productService.createProduct(product);
        return "redirect:/admin/products";  // Quay lại trang danh sách sản phẩm
    }


    // Edit product
    @GetMapping("/edit/{id}")
    public String editProductForm(@PathVariable("id") Long id, Model model) {
        Product product = productService.getProductById(id).orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/products/edit";  // Thymeleaf template
    }

    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable("id") Long id,
                                @RequestParam("name") String name,
                                @RequestParam("description") String description,
                                @RequestParam("thumbnail") String thumbnail,
                                @RequestParam("price") Double price,
                                @RequestParam("quantity") Integer quantity,
                                @RequestParam("category_id") Long categoryId) {

        // Lấy product hiện có từ database
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));

        // Cập nhật thông tin sản phẩm
        product.setName(name);
        product.setDescription(description);
        product.setThumbnail(thumbnail);
        product.setPrice(price);
        product.setQuantity(quantity);

        // Lấy category từ ID và gán vào product
        Category category = categoryService.getCategoryById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + categoryId));
        product.setCategory(category);

        // Cập nhật sản phẩm
        productService.updateProduct(product);
        return "redirect:/admin/products";
    }






    // Delete product
    @PostMapping("/destroy/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";  // Redirect to product list
    }
}
