package com.example.belle.data.service;

import com.example.belle.data.dao.UserDAO;
import com.example.belle.data.dto.UserDTO;
import com.example.belle.data.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    public List<UserDTO> getAllUsers() {
        return userDAO.getAllUsers().stream()
                .map(user -> new UserDTO(user.getId(), user.getEmail(), user.getRole()))
                .collect(Collectors.toList());
    }

    public Optional<UserDTO> getUserById(Long id) {
        return userDAO.getUserById(id)
                .map(user -> new UserDTO(user.getId(), user.getEmail(), user.getRole()));
    }

    public Optional<User> getUserByEmail(String email) {
        return userDAO.getUserByEmail(email);
    }

    public User createUser(User user) {
        return userDAO.createUser(user);
    }

    public User updateUser(Long id, User updatedUser) {
        return userDAO.getUserById(id).map(user -> {
            user.setEmail(updatedUser.getEmail());
            user.setPassword(updatedUser.getPassword());
            user.setRole(updatedUser.getRole());
            return userDAO.updateUser(user);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void deleteUser(Long id) {
        userDAO.deleteUser(id);
    }
}
