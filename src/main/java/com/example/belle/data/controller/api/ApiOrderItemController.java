package com.example.belle.data.controller.api;

import com.example.belle.data.model.OrderItem;
import com.example.belle.data.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-items")
@CrossOrigin(origins = "*")
public class ApiOrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    @GetMapping("/cart/{userId}")
    public ResponseEntity<List<OrderItem>> getCartItems(@PathVariable Long userId) {
        List<OrderItem> orderItems = orderItemService.getCartItems(userId);
        if (orderItems.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(orderItems);
    }
}
