package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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

                
                .requestMatchers("/", "/login", "/register", "/css/**", "/js/**", "/images/**").permitAll()

                
                .requestMatchers("/api/auth/**").permitAll()

                .requestMatchers("/admin/**").hasRole("SUPER_ADMIN")

                .requestMatchers("/actions/new", "/actions/edit/**").hasAnyRole("ORG_ADMIN", "SUPER_ADMIN")
                .requestMatchers("/organisations/approve/**").hasRole("SUPER_ADMIN")

                .requestMatchers(HttpMethod.POST, "/api/actions/**").hasAnyRole("ORG_ADMIN", "SUPER_ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/actions/**").hasAnyRole("ORG_ADMIN", "SUPER_ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/actions/**").hasAnyRole("ORG_ADMIN", "SUPER_ADMIN")
                .requestMatchers("/api/organisations/*/approve").hasRole("SUPER_ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/donations/**").hasRole("USER")

                // Tout le reste → connecté
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}