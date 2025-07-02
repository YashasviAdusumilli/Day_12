package org.example;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class EventController {
    private final EventService eventService;

    public EventController(Router router, MongoService mongoService) {
        this.eventService = new EventService(mongoService);

        // Public route to fetch events
        router.get("/events").handler(this::getAllEvents);

        // Admin-only route to create events
        router.post("/events")
                .handler(RoleMiddleware.requireRole("admin"))
                .handler(this::createEvent);
    }

    private void getAllEvents(RoutingContext ctx) {
        ctx.response()
                .putHeader("Content-Type", "application/json")
                .end(eventService.getAllEvents().encode());
    }

    private void createEvent(RoutingContext ctx) {
        JsonObject event = ctx.body().asJsonObject();
        eventService.createEvent(event);
        ctx.response()
                .setStatusCode(201)
                .putHeader("Content-Type", "application/json")
                .end(new JsonObject().put("message", "Event created").encode());
    }
}
