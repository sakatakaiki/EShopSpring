package com.example.belle.data.repository;

import com.example.belle.data.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


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

    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId ORDER BY " +
            "CASE WHEN :property = 'name' AND :order = 'asc' THEN p.name END ASC, " +
            "CASE WHEN :property = 'name' AND :order = 'desc' THEN p.name END DESC, " +
            "CASE WHEN :property = 'price' AND :order = 'asc' THEN p.price END ASC, " +
            "CASE WHEN :property = 'price' AND :order = 'desc' THEN p.price END DESC, " +
            "CASE WHEN :property = 'created_at' AND :order = 'asc' THEN p.createdAt END ASC, " +
            "CASE WHEN :property = 'created_at' AND :order = 'desc' THEN p.createdAt END DESC")
    List<Product> findProductsByCategorySorted(@Param("categoryId") Long categoryId,
                                               @Param("property") String property,
                                               @Param("order") String order);

    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) ORDER BY " +
            "CASE WHEN :property = 'name' AND :order = 'asc' THEN p.name END ASC, " +
            "CASE WHEN :property = 'name' AND :order = 'desc' THEN p.name END DESC, " +
            "CASE WHEN :property = 'price' AND :order = 'asc' THEN p.price END ASC, " +
            "CASE WHEN :property = 'price' AND :order = 'desc' THEN p.price END DESC, " +
            "CASE WHEN :property = 'created_at' AND :order = 'asc' THEN p.createdAt END ASC, " +
            "CASE WHEN :property = 'created_at' AND :order = 'desc' THEN p.createdAt END DESC")
    Page<Product> searchProductsByName(@Param("keyword") String keyword,
                                       @Param("property") String property,
                                       @Param("order") String order,
                                       Pageable pageable);




}
