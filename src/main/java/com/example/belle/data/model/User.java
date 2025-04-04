package com.example.belle.data.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;


    @Column(nullable = false)
    private String password;

    private String role;

    public User() {
    }

    public User(Long id, String email, String password, String role) {
        this.id = id;
        this.email = email;
        this.password = password; // Có thể đặt null để tránh trả về password
        this.role = role;
    }

    @Setter
    @Getter
    @Transient
    private String passwordConfirmation;

    // Getters và Setters cho các trường khác

}