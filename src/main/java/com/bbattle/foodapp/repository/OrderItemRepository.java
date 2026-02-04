package com.bbattle.foodapp.repository;

import com.bbattle.foodapp.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    // puoi aggiungere metodi custom se vuoi, per esempio:
    // List<OrderItem> findByOrderId(Long orderId);
}
