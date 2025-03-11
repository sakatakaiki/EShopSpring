package com.example.belle.data.dao;

import com.example.belle.data.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductDAO {
    List<Product> getAllProducts();
    Optional<Product> getProductById(Long id);
    Product createProduct(Product product);
    Product updateProduct(Product product);
    void deleteProduct(Long id);
    List<Product> getTop6ByPrice();
    List<Product> getTop8ByCreatedAt();
}
