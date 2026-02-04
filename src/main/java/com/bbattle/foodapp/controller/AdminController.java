package com.bbattle.foodapp.controller;

import com.bbattle.foodapp.model.Order;
import com.bbattle.foodapp.model.OrderItem;
import com.bbattle.foodapp.repository.OrderRepository;
import com.bbattle.foodapp.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    // GET ordini pendenti
    @GetMapping("/pending-orders")
    public List<Order> getPendingOrders() {
        return orderRepository.findByStatus("PENDING");
    }

    // GET totale guadagni
    @GetMapping("/earnings")
    public double getTotalEarnings() {
        List<OrderItem> items = orderItemRepository.findAll();
        return items.stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();
    }

    // GET numero ordini completati
    @GetMapping("/completed-orders-count")
    public long getCompletedOrdersCount() {
        return orderRepository.findByStatus("DONE").size();
    }
}
