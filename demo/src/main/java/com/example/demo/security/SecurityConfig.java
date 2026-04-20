package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Désactiver CSRF (pas nécessaire avec JWT)
            .csrf(csrf -> csrf.disable())

            // Définir qui peut accéder à quoi
            .authorizeHttpRequests(auth -> auth
                // Ces URLs sont accessibles sans connexion
                .requestMatchers(
                    "/api/auth/**",   // Login et inscription
                    "/h2-console/**"  // Base de données H2
                ).permitAll()
                // Tout le reste nécessite une connexion
                .anyRequest().authenticated()
            )

            // Ne pas utiliser les sessions (on utilise JWT)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // Autoriser H2 console dans un iframe
            .headers(headers -> headers.frameOptions().disable())

            // Ajouter notre filtre JWT avant le filtre de Spring
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Encodeur de mot de passe (BCrypt hash)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}