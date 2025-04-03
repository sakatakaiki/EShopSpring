package com.example.belle.data.service;

import com.example.belle.data.model.Order;
import com.example.belle.data.model.OrderItem;
import com.example.belle.data.model.Product;
import com.example.belle.data.model.User;
import com.example.belle.data.repository.OrderItemRepository;
import com.example.belle.data.repository.OrderRepository;
import com.example.belle.data.repository.ProductRepository;
import com.example.belle.data.repository.UserRepository;
import com.example.belle.data.dto.OrderStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    public Order addToCart(Long userId, Long productId, int quantity) {
        // Kiểm tra user
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User user = userOpt.get();

        // Kiểm tra product
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Tìm order "pending", nếu không có thì tạo mới
        Order order = orderRepository.findByUserAndStatus(user, "pending")
                .orElseGet(() -> {
                    Order newOrder = new Order(user, "pending");
                    return orderRepository.save(newOrder); // LƯU order ngay
                });

        // Tìm sản phẩm trong giỏ hàng
        Optional<OrderItem> existingItem = orderItemRepository.findByOrderAndProduct(order, product);

        if (existingItem.isPresent()) {
            // Nếu sản phẩm đã tồn tại, tăng số lượng
            OrderItem orderItem = existingItem.get();
            orderItem.setQuantity(orderItem.getQuantity() + quantity);
            orderItemRepository.save(orderItem);
        } else {
            // Nếu sản phẩm chưa có trong giỏ hàng, thêm mới
            OrderItem newItem = new OrderItem(order, product, quantity, product.getPrice());
            orderItemRepository.save(newItem);
        }

        return orderRepository.save(order); // Cập nhật order
    }

    public Order getPendingOrder(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return orderRepository.findByUserAndStatus(user, "pending").orElse(null);
    }

    public void checkout(Long userId) {
        Order order = getPendingOrder(userId);
        if (order != null) {
            order.setStatus("finished");
            orderRepository.save(order);

            // SAU KHI CHECKOUT, TẠO ORDER "PENDING" MỚI
            Order newOrder = new Order(order.getUser(), "pending");
            orderRepository.save(newOrder);
        } else {
            throw new RuntimeException("No pending order found");
        }
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Lấy đơn hàng theo ID
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public void updateOrderStatus(Long id, String status) {
        Order order = getOrderById(id);
        order.setStatus(status);
        orderRepository.save(order);
    }

    public List<OrderStatistics> getOrderStatistics() {
        // Lấy số lượng đơn hàng theo từng trạng thái
        long numberOrderToday = orderRepository.countByCreatedAtToday();
        long numberOrderAll = orderRepository.count();
        long numberOrderFinished = orderRepository.countByStatus("finished");
        long numberOrderPending = orderRepository.countByStatus("pending");

        return List.of(
                new OrderStatistics("Today Sale", numberOrderToday),
                new OrderStatistics("Total Sale", numberOrderAll),
                new OrderStatistics("Order Finished", numberOrderFinished),
                new OrderStatistics("Order Pending", numberOrderPending)
        );
    }

    public List<String> getDateList() {
        return orderRepository.findAllDates();
    }

    public List<Long> getOrderByDateList() {
        List<String> dateList = getDateList();
        List<Long> orderCounts = new ArrayList<>();

        for (String date : dateList) {
            orderCounts.add(orderRepository.findOrderByDate(date));
        }
        return orderCounts;
    }

    public List<String> getMonthList() {
        return orderRepository.findAllMonths();
    }

    public List<Long> getOrderByMonthList() {
        List<String> monthList = getMonthList();
        List<Long> orderCounts = new ArrayList<>();

        for (String month : monthList) {
            orderCounts.add(orderRepository.findOrderByMonth(month));
        }
        return orderCounts;
    }


}


