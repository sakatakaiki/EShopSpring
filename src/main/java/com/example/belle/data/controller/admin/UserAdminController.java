package com.example.belle.data.controller.admin;

import com.example.belle.data.model.User;
import com.example.belle.data.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/users")
public class UserAdminController {

    @Autowired
    private UserService userService;

    // 📝 HIỂN THỊ DANH SÁCH USER CHO ADMIN
    @GetMapping
    public String listUsers(Model model) {
        List<User> users = userService.getAllUsersAdmin();
        model.addAttribute("users", users);
        return "admin/users/index";
    }

    // 📝 FORM TẠO USER MỚI
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        return "admin/users/create";
    }

    // 📝 LƯU USER MỚI
    // 📝 LƯU USER MỚI
    @PostMapping("/store")
    public String storeUser(@ModelAttribute("user") @Valid User user,
                            BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "admin/users/create";
        }

        // Kiểm tra mật khẩu trống
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            result.rejectValue("password", "error.password", "Password is required");
            return "admin/users/create";
        }

        // Kiểm tra mật khẩu phải có ít nhất 6 ký tự
        if (user.getPassword().length() < 6) {
            result.rejectValue("password", "error.password.length", "Password must be at least 6 characters");
            return "admin/users/create";
        }

        // Kiểm tra xác nhận mật khẩu trùng khớp (không cần mã hóa `passwordConfirmation`)
        if (user.getPasswordConfirmation() == null || !user.getPasswordConfirmation().equals(user.getPassword())) {
            result.rejectValue("passwordConfirmation", "error.passwordConfirmation", "Passwords do not match");
            return "admin/users/create";
        }


        // Kiểm tra email trống hoặc không hợp lệ
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            result.rejectValue("email", "error.email", "Email is required");
            return "admin/users/create";
        }

        if (!user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            result.rejectValue("email", "error.email.invalid", "Invalid email format");
            return "admin/users/create";
        }

        userService.createUserAdmin(user);
        return "redirect:/admin/users?success=User created successfully";
    }


    // 📝 FORM CHỈNH SỬA USER
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<User> user = userService.getUserByIdAdmin(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "admin/users/edit";
        }
        return "redirect:/admin/users?error=User not found";
    }

    // 📝 CẬP NHẬT USER
    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable Long id,
                             @ModelAttribute("user") @Valid User user,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "admin/users/edit";
        }

        // Kiểm tra mật khẩu trống (chỉ kiểm tra nếu người dùng muốn thay đổi mật khẩu)
        if (user.getPassword() != null && !user.getPassword().isEmpty() && user.getPassword().length() < 6) {
            result.rejectValue("password", "error.password", "Password must be at least 6 characters");
            return "admin/users/edit";
        }

        // Kiểm tra xác nhận mật khẩu trùng khớp
        if (user.getPasswordConfirmation() != null && !user.getPasswordConfirmation().equals(user.getPassword())) {
            result.rejectValue("passwordConfirmation", "error.passwordConfirmation", "Passwords do not match");
            return "admin/users/edit";
        }

        userService.updateUserAdmin(id, user);
        return "redirect:/admin/users?success=User updated successfully";
    }

    // 📝 XÓA USER
    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users?success=User deleted successfully";
    }
}
