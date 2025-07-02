package org.example;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.UUID;

public class BookingService {
    private final MongoCollection<Document> bookings;
    private final MongoCollection<Document> events;
    private final EmailService emailService;

    public BookingService(MongoService mongoService, EmailService emailService) {
        MongoDatabase db = mongoService.getDatabase();
        this.bookings = db.getCollection("bookings");
        this.events = db.getCollection("events");
        this.emailService = emailService;
    }

    public String bookTicket(String eventId, String userEmail) {
        Document event = events.find(new Document("eventId", eventId)).first();
        if (event == null) {
            return null; // Event not found
        }

        String token = UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();

        Document booking = new Document("eventId", eventId)
                .append("userEmail", userEmail)
                .append("token", token);

        bookings.insertOne(booking);

        // Send confirmation email
        String subject = "🎟️ Your Ticket Token for " + event.getString("name");
        String content = "Hello " + userEmail + ",\n\nYour booking is confirmed!\nEvent: " + event.getString("name") +
                "\nToken: " + token + "\n\nShow this token at entry.\n\nRegards,\nEvent Team";

        emailService.sendEmail(userEmail, subject, content);

        return token;
    }
}
