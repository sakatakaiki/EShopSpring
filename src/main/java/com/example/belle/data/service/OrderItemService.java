package com.example.belle.data.service;

import com.example.belle.data.model.Order;
import com.example.belle.data.model.OrderItem;
import com.example.belle.data.model.User;
import com.example.belle.data.repository.OrderRepository;
import com.example.belle.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class OrderItemService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    public List<OrderItem> getCartItems(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = orderRepository.findByUserAndStatus(user, "pending").orElse(null);

        if (order != null) {
            return order.getOrderItems();
        } else {
            return Collections.emptyList();
        }
    }
}
