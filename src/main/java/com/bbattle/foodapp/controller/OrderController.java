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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@PreAuthorize("hasRole('ADMIN')")  // tutto qui dentro è admin-only

public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private FoodItemRepository foodItemRepository;

    @Autowired
    private UserRepository userRepository;

    // GET tutti gli ordini (admin)
    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // GET ordini di un utente
    @GetMapping("/user/{userId}")
    public List<Order> getOrdersByUser(@PathVariable Long userId) {
        return orderRepository.findByUserId(userId);
    }

    // POST creare un nuovo ordine
    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        // Salva l'ordine prima di salvare i suoi items
        Order savedOrder = orderRepository.save(order);

        // Salva ogni item con il prezzo corrente del foodItem
        for (OrderItem item : order.getItems()) {
            FoodItem food = foodItemRepository.findById(item.getFoodItem().getId())
                    .orElseThrow(() -> new RuntimeException("FoodItem non trovato"));
            item.setOrder(savedOrder);
            item.setPrice(food.getPrice()); // salva il prezzo corrente
            orderItemRepository.save(item);
        }

        // Aggiorna il totale
        double total = order.getItems().stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();
        savedOrder.setTotalAmount(total);
        return orderRepository.save(savedOrder);
    }

    // PUT aggiornare lo status di un ordine
    @PutMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam String status) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth == null || !auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(403).body("Non autorizzato");
        }

        // Trova ordine
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Ordine non trovato"));

        // Convalida stato
        try {
            OrderStatus newStatus = OrderStatus.valueOf(status);
            order.setStatus(newStatus);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Stato non valido");
        }

        return ResponseEntity.ok(orderRepository.save(order));
    }

    // DELETE cancellare un ordine
    @DeleteMapping("/{orderId}")
    public void deleteOrder(@PathVariable Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Ordine non trovato"));
        orderRepository.delete(order);
    }
}
