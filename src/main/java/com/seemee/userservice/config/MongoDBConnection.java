package com.seemee.userservice.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MongoDBConnection {

    private static String mongoUri;
    private static String databaseName;

    @Value("${spring.data.mongodb.uri}")
    public void setMongoUri(String uri) {
        MongoDBConnection.mongoUri = uri;
    }

    @Value("${spring.data.mongodb.database}")
    public void setDatabaseName(String database) {
        MongoDBConnection.databaseName = database;
    }

    public static MongoClient connect() {
        if (mongoUri == null || mongoUri.isEmpty()) {
            throw new RuntimeException("MongoDB URI not configured");
        }
        return MongoClients.create(mongoUri);
    }

    public static MongoDatabase getDatabase(MongoClient client, String databaseName) {
        return client.getDatabase(databaseName);
    }
}