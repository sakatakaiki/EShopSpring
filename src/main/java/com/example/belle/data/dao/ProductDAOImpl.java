package com.example.belle.data.dao;

import com.example.belle.data.model.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductDAOImpl implements ProductDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Product> getAllProducts() {
        return entityManager.createQuery("SELECT p FROM Product p", Product.class)
                .getResultList();
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        Product product = entityManager.find(Product.class, id);
        return Optional.ofNullable(product);
    }

    @Override
    @Transactional
    public Product createProduct(Product product) {
        entityManager.persist(product);
        return product;
    }

    @Override
    @Transactional
    public Product updateProduct(Product product) {
        return entityManager.merge(product);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product product = entityManager.find(Product.class, id);
        if (product != null) {
            entityManager.remove(product);
        }
    }

    @Override
    public List<Product> getTop6ByPrice() {
        return entityManager.createQuery("SELECT p FROM Product p ORDER BY p.price DESC", Product.class)
                .setMaxResults(6)
                .getResultList();
    }

    @Override
    public List<Product> getTop8ByCreatedAt() {
        return entityManager.createQuery("SELECT p FROM Product p ORDER BY p.createdAt DESC", Product.class)
                .setMaxResults(8)
                .getResultList();
    }
}
