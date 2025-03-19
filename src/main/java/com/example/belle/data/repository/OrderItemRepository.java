package com.example.belle.data.repository;

import com.example.belle.data.model.Order;
import com.example.belle.data.model.OrderItem;
import com.example.belle.data.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Optional<OrderItem> findByOrderAndProduct(Order order, Product product);
}
