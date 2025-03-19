package com.example.belle.data.repository;

import com.example.belle.data.model.Order;
import com.example.belle.data.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByUserAndStatus(User user, String status);

}
