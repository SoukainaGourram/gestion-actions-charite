package com.example.demo.security;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
            FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

       if (path.contains("/api/auth")) {
    filterChain.doFilter(request, response);
    return;
}

        // 🔐 Get Authorization header
        String authHeader = request.getHeader("Authorization");

        // ✅ Check Bearer token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            String token = authHeader.substring(7);

            // ✅ Validate token
            if (jwtUtil.isTokenValid(token)) {

                String email = jwtUtil.extractEmail(token);

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                new ArrayList<>()
                        );

                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        // ✅ Continue request
        filterChain.doFilter(request, response);
    }
}