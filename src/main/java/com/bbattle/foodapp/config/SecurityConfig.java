package com.bbattle.foodapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // disabilita CSRF per test API
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**").permitAll() // tutte le API /api/** libere
                        .anyRequest().authenticated()
                )
                .httpBasic(basic -> {}); // lambda vuota obbligatoria

        return http.build();
    }
}
