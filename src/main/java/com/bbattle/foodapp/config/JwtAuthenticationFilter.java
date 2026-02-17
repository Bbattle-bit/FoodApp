package com.bbattle.foodapp.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

@Override
protected void doFilterInternal(HttpServletRequest request,
                                HttpServletResponse response,
                                FilterChain filterChain)
        throws ServletException, java.io.IOException {

    System.out.println("🔥 FILTRO JWT ESEGUITO 🔥 per URL: " + request.getRequestURI());

    final String authHeader = request.getHeader("Authorization");

    if (authHeader == null || authHeader.isEmpty() || !authHeader.startsWith("Bearer ")) {
        filterChain.doFilter(request, response);
        return;
    }

    try {
        String token = authHeader.substring(7);
        System.out.println("TOKEN RICEVUTO DAL CLIENT: " + token);

        if(token.isBlank()){
            filterChain.doFilter(request, response);
            return;
        }

        String email = jwtService.extractEmail(token);
        String role = jwtService.extractRole(token);

        System.out.println("EMAIL DAL TOKEN: " + email);
        System.out.println("ROLE DAL TOKEN: " + role);

        if(email != null && role != null){
            // Gestione automatica ROLE_
            String grantedRole = role.startsWith("ROLE_") ? role : "ROLE_" + role;
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            email,
                            null,
                            List.of(new SimpleGrantedAuthority(grantedRole))
                    );

            SecurityContextHolder.getContext().setAuthentication(authToken);
            System.out.println("AUTH SETTATA CON: " + grantedRole);
        }

    } catch (JwtException e) {
        e.printStackTrace(); // così vedi se c'è qualche errore nel parsing
    }

    filterChain.doFilter(request, response);
}

    
}
