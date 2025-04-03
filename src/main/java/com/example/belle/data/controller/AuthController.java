package com.example.belle.data.controller;

import com.example.belle.data.model.User;
import com.example.belle.data.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    // Hiển thị trang đăng nhập
    @GetMapping("/login")
    public String showLoginPage(HttpSession session) {
        // Nếu đã đăng nhập, chuyển hướng về trang chủ
        if (session.getAttribute("user") != null) {
            return "redirect:/";
        }
        return "login";
    }

    // Xử lý đăng nhập
    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session, Model model) {

        Optional<User> userOpt = userService.getUserByEmail(email);
        if (userOpt.isPresent() && userService.checkPassword(password, userOpt.get().getPassword())) {
            User user = userOpt.get();
            session.setAttribute("user", user);
            session.setAttribute("role", user.getRole()); // Lưu role vào session

            // Chuyển hướng theo role
            if ("admin".equals(user.getRole())) {
                return "redirect:/admin/dashboard";
            }
            return "redirect:/";
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
    }


    // Xử lý logout
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();  // Xóa session
        }
        return "redirect:/";
    }

    // Hiển thị trang đăng ký
    @GetMapping("/register")
    public String showRegisterPage(HttpSession session) {
        if (session.getAttribute("user") != null) {
            return "redirect:/"; // Nếu đã đăng nhập, quay về trang chủ
        }
        return "register";
    }

    // Xử lý đăng ký
    @PostMapping("/register")
    public String register(@RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String confirmPassword,
                           Model model) {
        // Kiểm tra mật khẩu nhập lại
        if (!password.equals(confirmPassword)) {
            model.addAttribute("passwordError", "Passwords do not match!");
            return "register";
        }

        // Kiểm tra email đã tồn tại chưa
        if (userService.getUserByEmail(email).isPresent()) {
            model.addAttribute("error", "Email already exists");
            return "register";
        }

        // Tạo user mới
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setRole("user");

        userService.createUser(newUser);
        return "redirect:/login";
    }


}
