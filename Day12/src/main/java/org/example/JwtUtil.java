package org.example;

import io.jsonwebtoken.*;
import io.vertx.ext.web.RoutingContext;

import java.util.Date;

public class JwtUtil {
    private static final String SECRET_KEY = "This_is_Part_Of_Day_12_Project";

    public static String generateToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public static Claims decodeToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    public static String extractEmail(RoutingContext ctx) {
        Claims claims = ctx.get("userClaims");
        if (claims != null) {
            return claims.getSubject();
        }
        return null;
    }
}
