package com.example.belle.data.service;

import com.example.belle.data.model.Product;
import com.example.belle.data.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;


@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public List<Product> getTop6ByPrice() {
        return productRepository.findTop6ByOrderByPriceDesc();
    }

    public List<Product> getTop8ByCreatedAt() {
        return productRepository.findTop8ByOrderByCreatedAtDesc();
    }

    public List<Product> getProductsByCategoryName(String categoryName) {
        return productRepository.findByCategory_Name(categoryName);
    }

    public List<Product> searchProducts(String keyword) {
        return productRepository.searchProductsByName(keyword);
    }

    public List<Product> getTopProducts() {
        return productRepository.findTop6ByOrderByViewDesc();
    }

    public Page<Product> getProductsByCategorySorted(Long categoryId, String property, String order, int page, int size) {
        Pageable pageable = PageRequest.of(page, size,
                order.equals("asc") ? Sort.by(property).ascending() : Sort.by(property).descending());
        Page<Product> result = productRepository.findByCategoryId(categoryId, pageable);

        // In ra console để kiểm tra dữ liệu
        System.out.println("Category ID: " + categoryId);
        System.out.println("Products fetched: " + result.getTotalElements());
        result.getContent().forEach(p -> System.out.println("Product: " + p.getName() + " - " + p.getThumbnail()));

        return result;
    }


}