package com.bbattle.foodapp.controller;

import com.bbattle.foodapp.model.FoodItem;
import com.bbattle.foodapp.repository.FoodItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/food")
@CrossOrigin(origins = "http://localhost:5173") // React + Vite
public class FoodItemController {
    @Autowired
    private FoodItemRepository foodItemRepository;

    @GetMapping
    public List<FoodItem> getAllFoodItems(){
        return foodItemRepository.findAll();
    }

    @PostMapping
    public FoodItem createFoodItem(@RequestBody FoodItem foodItem){
        return foodItemRepository.save(foodItem);
    }
}
