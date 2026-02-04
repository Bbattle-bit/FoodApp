package com.bbattle.foodapp.controller;

import com.bbattle.foodapp.model.User;
import com.bbattle.foodapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")

//Con questo puoi fare:
//GET /api/users → vedere tutti gli utenti
//POST /api/users → creare un nuovo utente inviando JSON
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<User> getAllUser(){
        return userRepository.findAll();
    }

    @PostMapping
    public User createUser(@RequestBody User user){
        return userRepository.save(user);
    }
}
