package org.example;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.bson.Document;
import org.mindrot.jbcrypt.BCrypt;

import com.mongodb.client.MongoCollection;

public class AuthController {
    private final MongoCollection<Document> users;

    public AuthController(Router router, MongoService mongoService) {
        this.users = mongoService.getDatabase().getCollection("users");

        router.post("/login").handler(this::login);
    }

    private void login(RoutingContext ctx) {
        JsonObject body = ctx.body().asJsonObject();
        String email = body.getString("email");
        String password = body.getString("password");

        if (email == null || password == null) {
            ctx.response().setStatusCode(400).end("Email and password are required");
            return;
        }

        Document userDoc = users.find(new Document("email", email)).first();
        if (userDoc == null) {
            ctx.response().setStatusCode(401).end("Invalid credentials");
            return;
        }

        String hashedPassword = userDoc.getString("password");
        if (!BCrypt.checkpw(password, hashedPassword)) {
            ctx.response().setStatusCode(401).end("Invalid credentials");
            return;
        }

        String role = userDoc.getString("role");
        String token = JwtUtil.generateToken(email, role);

        JsonObject response = new JsonObject()
                .put("message", "Login successful")
                .put("token", token)
                .put("role", role);

        ctx.response().putHeader("Content-Type", "application/json")
                .end(response.encode());
    }
}
