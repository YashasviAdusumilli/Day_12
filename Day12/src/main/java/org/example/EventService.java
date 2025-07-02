package org.example;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

public class EventService {
    private final MongoCollection<Document> eventCollection;

    public EventService(MongoService mongoService) {
        MongoDatabase database = mongoService.getDatabase();
        this.eventCollection = database.getCollection("events");
    }

    public void createEvent(JsonObject eventJson) {
        Document eventDoc = Document.parse(eventJson.encode());
        eventCollection.insertOne(eventDoc);
    }

    public JsonArray getAllEvents() {
        List<JsonObject> events = new ArrayList<>();
        for (Document doc : eventCollection.find()) {
            JsonObject event = new JsonObject(doc.toJson());
            event.put("_id", doc.getObjectId("_id").toHexString()); // Convert ObjectId to string
            events.add(event);
        }
        return new JsonArray(events);
    }
}
