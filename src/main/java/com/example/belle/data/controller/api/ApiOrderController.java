package com.example.belle.data.controller.api;

import com.example.belle.data.model.Order;
import com.example.belle.data.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class ApiOrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/addToCart/{userId}/{productId}")
    public ResponseEntity<Order> addToCart(
            @PathVariable Long userId,
            @PathVariable Long productId,
            @RequestParam int quantity
    ) {
        return ResponseEntity.ok(orderService.addToCart(userId, productId, quantity));
    }


    @GetMapping("/pending/{userId}")
    public ResponseEntity<Order> getPendingOrder(@PathVariable Long userId) {
        Order order = orderService.getPendingOrder(userId);
        if (order != null) {
            return ResponseEntity.ok(order);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/checkout/{userId}")
    public ResponseEntity<String> checkout(@PathVariable Long userId) {
        try {
            orderService.checkout(userId);
            return ResponseEntity.ok("Checkout successful");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



}