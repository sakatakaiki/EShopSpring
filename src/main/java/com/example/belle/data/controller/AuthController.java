package com.example.belle.data.controller;

import com.example.belle.data.model.User;
import com.example.belle.data.service.TwilioService;
import com.example.belle.data.service.UserService;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Value;

import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private TwilioService twilioService;

    @Value("${twilio.verifyServiceSid}")
    private String verifyServiceSid;

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
    public String showRegisterPage(HttpSession session, Model model) {
        if (session.getAttribute("user") != null) {
            return "redirect:/";
        }
        model.addAttribute("showOtpField", false);
        model.addAttribute("otpSent", false);
        return "register";
    }

    // Xử lý đăng ký
    @PostMapping("/register")
    public String register(@RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String confirmPassword,
                           @RequestParam String otp,
                           HttpServletRequest request,
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

        // Kiểm tra OTP
        HttpSession session = request.getSession();
        // Make sure the verificationSid exists in the session and is valid
        String verificationSid = (String) session.getAttribute("verificationSid");
        if (verificationSid == null || verificationSid.isEmpty()) {
            model.addAttribute("error", "Verification SID not found.");
            return "register";
        }

        System.out.println("Verification SID: " + verificationSid);

        VerificationCheck verificationCheck = VerificationCheck.creator(verifyServiceSid) // Use verifyServiceSid from properties
                .setTo("+84374178451")
                .setCode(otp)
                .create();

// Check if the OTP is correct
        if ("approved".equals(verificationCheck.getStatus())) {
            // Continue registration process
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setPassword(password);
            newUser.setRole("user");
            userService.createUser(newUser);
            return "redirect:/login";  // Redirect after successful registration
        } else {
            // Handle OTP failure
            model.addAttribute("otpError", "Invalid OTP!");
            return "register";  // Return to the registration page
        }
    }

    // Sửa lại phương thức gửi OTP
    @PostMapping("/sendOtp")
    public String sendOtp(@RequestParam String phoneNumber, HttpServletRequest request, Model model) {
        Verification verification = twilioService.sendOtp(phoneNumber);

        // Lưu SID của OTP vào session
        HttpSession session = request.getSession();
        session.setAttribute("verificationSid", verification.getSid());

        // Chuyển đổi giao diện
        model.addAttribute("message", "OTP sent to your phone!");
        model.addAttribute("showOtpField", true);  // Hiển thị trường OTP
        model.addAttribute("otpSent", true);
        return "register";
    }



}
