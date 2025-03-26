package com.example.belle.data.repository;

import com.example.belle.data.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT c FROM Category c ORDER BY SIZE(c.products) DESC")
    List<Category> findTop5CategoriesByProductCount(Pageable pageable);

}