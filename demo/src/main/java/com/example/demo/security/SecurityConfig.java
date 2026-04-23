package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
           
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                
                .requestMatchers("/api/auth/**","/h2-console/**"  ).permitAll()
                 // ✅ Lecture des actions et organisations → tout le monde (connecté)
                .requestMatchers(HttpMethod.GET, "/api/actions/**").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/organisations/**").authenticated()
 
                // ✅ Créer/modifier/supprimer une action → ORG_ADMIN ou SUPER_ADMIN
                .requestMatchers(HttpMethod.POST, "/api/actions/**").hasAnyRole("ORG_ADMIN", "SUPER_ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/actions/**").hasAnyRole("ORG_ADMIN", "SUPER_ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/actions/**").hasAnyRole("ORG_ADMIN", "SUPER_ADMIN")
 
                // ✅ Approuver une organisation → SUPER_ADMIN uniquement
                .requestMatchers("/api/organisations/*/approve").hasRole("SUPER_ADMIN")
 
                // ✅ Créer une organisation → tout utilisateur connecté
                .requestMatchers(HttpMethod.POST, "/api/organisations/**").authenticated()
 
                // ✅ Gérer les utilisateurs → SUPER_ADMIN uniquement
                .requestMatchers("/api/user/**").authenticated()
 
                // ✅ Faire un don → USER
                .requestMatchers(HttpMethod.POST, "/api/donations/**").hasRole("USER")
 
                // Tout le reste → connecté
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .headers(headers -> headers.frameOptions().disable())
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
 
        return http.build();
    }
 
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}