package com.example.belle.data.repository;

import com.example.belle.data.model.Order;
import com.example.belle.data.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByUserAndStatus(User user, String status);
    long countByStatus(String status);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.createdAt >= CURRENT_DATE")
    long countByCreatedAtToday();


    List<Order> findByStatus(String status);

    // Lấy danh sách các ngày mà đơn hàng được tạo
    @Query("SELECT DISTINCT FUNCTION('DATE', o.createdAt) FROM Order o WHERE o.createdAt IS NOT NULL ORDER BY o.createdAt")
    List<String> findAllDates();

    // Lấy số lượng đơn hàng được tạo theo từng ngày
    @Query("SELECT COUNT(o) FROM Order o WHERE FUNCTION('DATE', o.createdAt) = ?1")
    long findOrderByDate(String date);

    // Lấy danh sách các tháng mà đơn hàng được tạo
    @Query("SELECT DISTINCT FUNCTION('MONTH', o.createdAt) FROM Order o WHERE o.createdAt IS NOT NULL ORDER BY o.createdAt")
    List<String> findAllMonths();

    // Lấy số lượng đơn hàng theo từng tháng
    @Query("SELECT COUNT(o) FROM Order o WHERE FUNCTION('MONTH', o.createdAt) = ?1")
    long findOrderByMonth(String month);
}
