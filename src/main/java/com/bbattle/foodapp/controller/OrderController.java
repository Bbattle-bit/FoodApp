package com.bbattle.foodapp.controller;

import com.bbattle.foodapp.model.Order;
import com.bbattle.foodapp.model.OrderItem;
import com.bbattle.foodapp.model.FoodItem;
import com.bbattle.foodapp.model.User;
import com.bbattle.foodapp.repository.OrderRepository;
import com.bbattle.foodapp.repository.OrderItemRepository;
import com.bbattle.foodapp.repository.FoodItemRepository;
import com.bbattle.foodapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public Order updateOrderStatus(@PathVariable Long orderId, @RequestParam String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Ordine non trovato"));
        order.setStatus(status);
        return orderRepository.save(order);
    }
}
