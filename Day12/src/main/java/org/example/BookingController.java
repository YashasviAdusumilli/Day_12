package org.example;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class BookingController {
    private final BookingService bookingService;

    public BookingController(Router router, MongoService mongoService, EmailService emailService) {
        this.bookingService = new BookingService(mongoService, emailService);

        // Secure route with JWT
        router.post("/book")
                .handler(RoleMiddleware.requireAuth())
                .handler(this::handleBooking);
    }

    private void handleBooking(RoutingContext ctx) {
        JsonObject body = ctx.body().asJsonObject();
        String eventId = body.getString("eventId");
        String userEmail = JwtUtil.extractEmail(ctx);

        if (eventId == null || userEmail == null) {
            ctx.response().setStatusCode(400).end("Missing event ID or user token");
            return;
        }

        String token = bookingService.bookTicket(eventId, userEmail);

        if (token == null) {
            ctx.response().setStatusCode(404).end("Event not found");
        } else {
            ctx.response()
                    .putHeader("Content-Type", "application/json")
                    .end(new JsonObject()
                            .put("message", "Booking confirmed. Token sent to email.")
                            .put("token", token)
                            .encode());
        }
    }
}
