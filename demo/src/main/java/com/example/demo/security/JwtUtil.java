package com.example.demo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // Clé secrète pour signer les tokens (comme un tampon officiel)
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Durée de validité du token : 24 heures
    private final long EXPIRATION = 86400000;

    // Créer un token pour un utilisateur connecté
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)          // L'email de l'utilisateur
                .setIssuedAt(new Date())    // Date de création
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION)) // Date d'expiration
                .signWith(key)              // Signature avec la clé secrète
                .compact();
    }

    // Extraire l'email depuis un token
    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Vérifier si le token est encore valide
    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}