package com.example.demo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // Récupérer le header "Authorization" de la requête
        String authHeader = request.getHeader("Authorization");

        // Vérifier si le header commence par "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            // Extraire le token (enlever "Bearer ")
            String token = authHeader.substring(7);

            // Vérifier si le token est valide
            if (jwtUtil.isTokenValid(token)) {

                // Extraire l'email du token
                String email = jwtUtil.extractEmail(token);

                // Dire à Spring Security que cet utilisateur est authentifié
                UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(email, null, new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        // Continuer la requête
        filterChain.doFilter(request, response);
    }
}