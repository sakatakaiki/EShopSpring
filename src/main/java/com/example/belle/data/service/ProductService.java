package com.example.belle.data.service;

import com.example.belle.data.model.Product;
import com.example.belle.data.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

}