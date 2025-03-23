package com.example.belle.data.service;

import com.example.belle.data.dto.UserDTO;
import com.example.belle.data.model.User;
import com.example.belle.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Lấy tất cả user và chuyển sang UserDTO
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserDTO(user.getId(), user.getEmail(), user.getRole()))
                .collect(Collectors.toList());
    }

    // Tìm user theo ID và chuyển sang UserDTO
    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id)
                .map(user -> new UserDTO(user.getId(), user.getEmail(), user.getRole()));
    }

    // Tìm user theo email
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Tạo user mới
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // Cập nhật user
    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setEmail(updatedUser.getEmail());
                    user.setPassword(updatedUser.getPassword());
                    user.setRole(updatedUser.getRole());
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Xóa user theo ID
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }


}