package com.bbattle.foodapp.repository;
import com.bbattle.foodapp.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
//Questo ti permetterà di recuperare tutti gli ordini di un utente.
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
    List<Order> findByStatus(String status);
}
