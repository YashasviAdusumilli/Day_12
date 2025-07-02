package org.example;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import io.jsonwebtoken.Claims;

public class RoleMiddleware {

    // Middleware to check if user is authenticated
    public static Handler<RoutingContext> requireAuth() {
        return ctx -> {
            String token = ctx.request().getHeader("Authorization");
            if (token == null || !token.startsWith("Bearer ")) {
                ctx.response().setStatusCode(401).end("Unauthorized: Token missing or invalid");
                return;
            }

            token = token.substring(7); // remove "Bearer "
            try {
                Claims claims = JwtUtil.decodeToken(token);
                ctx.put("userClaims", claims);
                ctx.next();
            } catch (Exception e) {
                ctx.response().setStatusCode(401).end("Unauthorized: Invalid token");
            }
        };
    }

    // Middleware to check if user has the required role (admin/user)
    public static Handler<RoutingContext> requireRole(String requiredRole) {
        return ctx -> {
            Claims claims = ctx.get("userClaims");
            if (claims == null) {
                ctx.response().setStatusCode(403).end("Forbidden: No claims found");
                return;
            }

            String role = claims.get("role", String.class);
            if (role == null || !role.equals(requiredRole)) {
                ctx.response().setStatusCode(403).end("Forbidden: Insufficient permissions");
                return;
            }

            ctx.next();
        };
    }
}
