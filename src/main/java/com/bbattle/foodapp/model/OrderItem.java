package com.bbattle.foodapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;
    private double price; // prezzo totale = quantity * foodItem.price

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference // per evitare problemi di serializzazione JSON
    private Order order;

    @ManyToOne
    @JoinColumn(name = "food_item_id")
    private FoodItem foodItem;
}
