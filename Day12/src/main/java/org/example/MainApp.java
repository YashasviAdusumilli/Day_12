package org.example;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class MainApp {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        Router router = Router.router(vertx);

        router.route().handler(BodyHandler.create());

        MongoService mongoService = new MongoService();
        EmailService emailService = new EmailService();

        UserController userController = new UserController(router, mongoService, emailService);
        AuthController authController = new AuthController(router, mongoService);
        EventController eventController = new EventController(router, mongoService);
        BookingController bookingController = new BookingController(router, mongoService, emailService);

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8888, result -> {
                    if (result.succeeded()) {
                        System.out.println("✅ Server started on port 8888");
                    } else {
                        System.out.println("❌ Failed to start server: " + result.cause());
                    }
                });
    }
}
