package com.bbattle.foodapp.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

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

    final String authHeader = request.getHeader("Authorization");

    if (authHeader == null || authHeader.isEmpty() || !authHeader.startsWith("Bearer ")) {
        // Nessun token: ignora il filtro
        filterChain.doFilter(request, response);
        return;
    }

    try {
        String token = authHeader.substring(7);
        if(token.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        String email = jwtService.extractEmail(token);
        String role = jwtService.extractRole(token);

        if (email != null) {
            List<GrantedAuthority> authorities =
                    List.of(new SimpleGrantedAuthority("ROLE_" + role));

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            email,
                            null,
                            authorities
                    );

            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

    } catch (io.jsonwebtoken.JwtException e) {
        // Token malformato o scaduto → ignora il filtro (non bloccare la richiesta)
        // puoi anche loggare qui: e.printStackTrace();
    }

    filterChain.doFilter(request, response);
}

    
}
