package com.bbattle.foodapp.config;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {
    
    // Chiave segreta per la firma del token JWT (deve essere sicura e mantenuta segreta)
    private static final String SECRET = "mysecretkeymysecretkeymysecretkey"; // Deve essere almeno di 256 bit (32 caratteri)

    // Ottiene la chiave di firma per il token JWT
    private Key getSignKey(){
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    // Genera un token JWT con l'email e il ruolo dell'utente
    public String generateToken(String email, String role){
        return Jwts.builder()
            .setSubject(email)
            .claim("role", role) // Aggiungi il ruolo come claim
            .setIssuedAt(new Date())
            //.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 ora di validità
            .signWith(getSignKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    // Estrae il ruolo dal token JWT
    public String extractRole(String token){
        return extractAllClaims(token).get("role", String.class);
    }

    // Estrae l'email (subject) dal token JWT
    public String extractEmail(String token){
        return extractAllClaims(token).getSubject();
    }

    // Estrae tutte le informazioni (claims) dal token JWT
    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
            .setSigningKey(getSignKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }


}
