package com.example.belle.data.controller;

import com.example.belle.data.model.Order;
import com.example.belle.data.model.OrderItem;
import com.example.belle.data.model.User;
import com.example.belle.data.service.OrderService;
import com.example.belle.data.service.OrderItemService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        List<OrderItem> cartItems = orderItemService.getCartItems(user.getId());
        double totalPrice = cartItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
        Order order = orderService.getPendingOrder(user.getId());
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("cart", order);

        return "cart";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId, @RequestParam(defaultValue = "1") int quantity, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        orderService.addToCart(user.getId(), productId, quantity);
        return "redirect:/cart";
    }

    @GetMapping("/update")
    public String updateQuantity(@RequestParam Long itemId, @RequestParam String action) {
        orderItemService.updateQuantity(itemId, action);
        return "redirect:/cart";
    }

    @GetMapping("/remove")
    public String removeFromCart(@RequestParam Long itemId) {
        orderItemService.removeItem(itemId);
        return "redirect:/cart";
    }

    @PostMapping("/checkout")
    public String checkout(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        orderService.checkout(user.getId());
        return "redirect:/cart";
    }

}