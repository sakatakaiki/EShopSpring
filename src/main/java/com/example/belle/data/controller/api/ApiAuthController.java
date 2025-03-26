package com.example.belle.data.controller.api;

import com.example.belle.data.dto.UserDTO;
import com.example.belle.data.model.User;
import com.example.belle.data.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/auth")
public class ApiAuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody User user) {
        Optional<User> existingUser = userService.getUserByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        User newUser = userService.createUser(user);
        UserDTO userDTO = new UserDTO(newUser.getId(), newUser.getEmail(), newUser.getRole());
        return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        Optional<User> foundUser = userService.getUserByEmail(user.getEmail());
        if (foundUser.isPresent()) {
            String hashedPassword = foundUser.get().getPassword();
            String rawPassword = user.getPassword();

            System.out.println("Raw password: " + rawPassword);
            System.out.println("Hashed password from DB: " + hashedPassword);
            System.out.println("BCrypt check: " + BCrypt.checkpw(rawPassword, hashedPassword));

            if (userService.checkPassword(rawPassword, hashedPassword)) {
                UserDTO userDTO = new UserDTO(foundUser.get().getId(), foundUser.get().getEmail(), foundUser.get().getRole());
                return ResponseEntity.ok(userDTO);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
}
