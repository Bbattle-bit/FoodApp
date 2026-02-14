package com.bbattle.foodapp.controller;

import com.bbattle.foodapp.config.JwtService;
import com.bbattle.foodapp.model.User;
import com.bbattle.foodapp.repository.UserRepository;

import io.jsonwebtoken.Jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/users")

//Con questo puoi fare:
//GET /api/users → vedere tutti gli utenti
//POST /api/users → creare un nuovo utente inviando JSON
public class UserController {

    @Autowired
    private UserRepository userRepository;

    //Registrazione di un nuovo utente
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user){

        //controlla se l'email è già registrata
        if(userRepository.existsByEmail(user.getEmail())){
            return ResponseEntity.badRequest().body("Email già registrata");
        }

        //Salva il nuovo utente
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "success"));

    }
    @Autowired
    private JwtService jwtService;
    //Login
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User loginRequest){

        Optional<User> user = userRepository.findByEmail(loginRequest.getEmail());

        if(user.isEmpty() || !user.get().getPassword().equals(loginRequest.getPassword())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email o password errati");
        }

        //Genera un token JWT con l'email e il ruolo dell'utente
        String token = jwtService.generateToken(user.get().getEmail(), user.get().getRole());

        return ResponseEntity.ok(Map.of(
            "message", "success",
            "token", token
            //"role", user.get().getRole() //invia anche il ruolo dell'utente al frontend
        ));

    }
}
