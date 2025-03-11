package com.example.belle.data.service;

import com.example.belle.data.dao.ProductDAO;
import com.example.belle.data.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductDAO productDAO;

    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }

    public Optional<Product> getProductById(Long id) {
        return productDAO.getProductById(id);
    }

    public Product createProduct(Product product) {
        return productDAO.createProduct(product);
    }

    public Product updateProduct(Long id, Product updatedProduct) {
        return productDAO.getProductById(id).map(product -> {
            product.setName(updatedProduct.getName());
            product.setDescription(updatedProduct.getDescription());
            product.setThumbnail(updatedProduct.getThumbnail());
            product.setPrice(updatedProduct.getPrice());
            product.setQuantity(updatedProduct.getQuantity());
            product.setCategory_id(updatedProduct.getCategory_id());
            product.setView(updatedProduct.getView());
            return productDAO.updateProduct(product);
        }).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public void deleteProduct(Long id) {
        productDAO.deleteProduct(id);
    }

    public List<Product> getTop6ByPrice() {
        return productDAO.getTop6ByPrice();
    }

    public List<Product> getTop8ByCreatedAt() {
        return productDAO.getTop8ByCreatedAt();
    }
}
