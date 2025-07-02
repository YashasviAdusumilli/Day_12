package org.example;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoService {
    private final MongoClient mongoClient;
    private final MongoDatabase database;

    public MongoService() {
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        database = mongoClient.getDatabase("event_ticket_system"); // your DB name
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }
}
