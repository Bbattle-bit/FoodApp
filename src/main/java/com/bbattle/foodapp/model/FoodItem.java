package com.bbattle.foodapp.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "food_items")
public class FoodItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private double price;
    private String ImageUrl; // link su AWS S3
}
