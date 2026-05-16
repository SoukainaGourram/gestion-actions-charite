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

    
    public String generateToken(String email,String role) {
        return Jwts.builder()
                .setSubject(email)         
                .setIssuedAt(new Date())  
                .claim("role", role)  
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION)) 
                .signWith(key)              
                .compact();
    }

    // Extraire l'email depuis un token
    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }
    public String extractRole(String token) {
        return getClaims(token).get("role", String.class);
    }
    // Vérifier si le token est encore valide
    public boolean isTokenValid(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    // Méthode privée commune pour parser le token
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
