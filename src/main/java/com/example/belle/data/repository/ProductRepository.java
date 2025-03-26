package com.example.belle.data.repository;

import com.example.belle.data.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Lấy 6 sản phẩm có giá cao nhất
    List<Product> findTop6ByOrderByPriceDesc();

    List<Product> findTop6ByOrderByViewDesc();
    // Lấy 8 sản phẩm mới nhất theo thời gian tạo
    List<Product> findTop8ByOrderByCreatedAtDesc();

    List<Product> findByCategory_Name(String categoryName);

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Product> searchProductsByName(@org.springframework.data.repository.query.Param("keyword") String keyword);
}
