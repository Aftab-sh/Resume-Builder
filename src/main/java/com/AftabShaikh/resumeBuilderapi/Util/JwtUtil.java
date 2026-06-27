package com.AftabShaikh.resumeBuilderapi.Util;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import com.AftabShaikh.resumeBuilderapi.Entity.UserPrincipal;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component 
public class JwtUtil {

    @Value("${jwt.secret}") 
    private String jwtSecret;
    
    @Value("${jwt.expiration}") 
    private long jwtExpiration;
    
    // 🚀 Method A: Jab Roles aur Permissions ke sath Token banana ho (Login/Admin flow)
    public String generateToken(UserPrincipal userPrincipal) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);
        
        List<String> authorities = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", authorities); 

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userPrincipal.getUsername()) 
                .setIssuedAt(now)  
                .setExpiration(expiryDate)
                .signWith(getSigningKey()) 
                .compact(); 
    }

    // 🚀 Method B: Overloaded Method - Jab sirf simple Subject/String se Token banana ho (Register flow)
    public String generateToken(String subject) 
    {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);
        
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    // ✅ FIXED: Syntax Error bracket yahan open kar diya hai
    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) 
                .build()
                .parseClaimsJws(token) 
                .getBody();

        return claims.getSubject();
    }
    
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
    
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token) 
                    .getBody();
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true; 
        }
    }
}