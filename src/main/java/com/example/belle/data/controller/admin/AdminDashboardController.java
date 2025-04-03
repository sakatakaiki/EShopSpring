package com.example.belle.data.controller.admin;

import com.example.belle.data.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminDashboardController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/admin/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        Object user = session.getAttribute("user");
        if (user == null || !"admin".equals(session.getAttribute("role"))) {
            return "redirect:/login";
        }

        // Lấy thống kê đơn hàng
        model.addAttribute("orderStats", orderService.getOrderStatistics());
        model.addAttribute("orderList", orderService.getAllOrders());

        // Lấy danh sách ngày, tháng và số lượng đơn hàng
        model.addAttribute("dateList", orderService.getDateList());
        model.addAttribute("orderByDateList", orderService.getOrderByDateList());
        model.addAttribute("monthList", orderService.getMonthList());
        model.addAttribute("orderByMonthList", orderService.getOrderByMonthList());

        return "admin/dashboard";
    }

}
