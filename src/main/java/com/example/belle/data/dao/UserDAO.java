package com.example.belle.data.dao;

import com.example.belle.data.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO {
    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    Optional<User> getUserByEmail(String email);
    User createUser(User user);
    User updateUser(User user);
    void deleteUser(Long id);
}
