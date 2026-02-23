package com.bbattle.foodapp.controller;

import com.bbattle.foodapp.model.Order;
import com.bbattle.foodapp.model.OrderItem;
import com.bbattle.foodapp.model.OrderStatus;
import com.bbattle.foodapp.model.FoodItem;
import com.bbattle.foodapp.model.User;
import com.bbattle.foodapp.repository.OrderRepository;
import com.bbattle.foodapp.repository.OrderItemRepository;
import com.bbattle.foodapp.repository.FoodItemRepository;
import com.bbattle.foodapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private FoodItemRepository foodItemRepository;

    @Autowired
    private UserRepository userRepository;

    // ================== ADMIN ==================
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long orderId,
                                               @RequestParam String status) {

        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(403).body("Non autorizzato");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Ordine non trovato"));

        try {
            OrderStatus newStatus = OrderStatus.valueOf(status);
            order.setStatus(newStatus);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Stato non valido");
        }

        return ResponseEntity.ok(orderRepository.save(order));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{orderId}")
    public void deleteOrder(@PathVariable Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Ordine non trovato"));
        orderRepository.delete(order);
    }

    // ================== UTENTI ==================
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user/{userId}")
    public List<Order> getOrdersByUser(@PathVariable Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        Order savedOrder = orderRepository.save(order);

        for (OrderItem item : order.getItems()) {
            FoodItem food = foodItemRepository.findById(item.getFoodItem().getId())
                    .orElseThrow(() -> new RuntimeException("FoodItem non trovato"));
            item.setOrder(savedOrder);
            item.setPrice(food.getPrice());
            orderItemRepository.save(item);
        }

        double total = order.getItems().stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();
        savedOrder.setTotalAmount(total);

        return orderRepository.save(savedOrder);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/latest")
    public ResponseEntity<Order> getLatestOrder(Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        Order latestOrder = orderRepository.findFirstByUserOrderByOrderDateDesc(user)
                .orElse(null);

        if (latestOrder == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(latestOrder);
    }
}