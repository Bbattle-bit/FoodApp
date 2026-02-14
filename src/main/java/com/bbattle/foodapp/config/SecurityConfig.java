package com.bbattle.foodapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // disabilita CSRF
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/users/login").permitAll() // login libero
                .requestMatchers("/api/food").permitAll()        // menu pubblico
                .anyRequest().authenticated()                    // resto protetto
            );

        return http.build();
    }
}
