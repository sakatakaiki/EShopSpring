package com.example.belle.data.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
    private String description;
    private String thumbnail;
    private double price;
    private int quantity;

    @Column(name = "category_id")
    private int category_id;

    private LocalDateTime createdAt;
    private int view;
}
