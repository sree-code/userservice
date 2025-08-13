package com.seemee.userservice.repository.Impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.seemee.userservice.config.MongoDBConnection;
import com.seemee.userservice.constants.SeeMeeConstants;
import com.seemee.userservice.model.UserSessionLog;
import com.seemee.userservice.repository.UserSessionLogRepository;
import org.bson.Document;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Repository
public class UserSessionLogRepositoryImpl implements UserSessionLogRepository {
    Logger logger = Logger.getLogger(UserSessionLogRepositoryImpl.class.getName());

    @Override
    public void createSessionLog(UserSessionLog sessionLog) {
        String methodName = "createSessionLog";
        logger.info("Entering into " + methodName);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        try (MongoClient client = MongoDBConnection.connect()) {
            // Get the database
            MongoDatabase database = MongoDBConnection.getDatabase(client, SeeMeeConstants.SeeMee);

            // Get the collection
            MongoCollection<Document> collection = database.getCollection("userSessionLogs");

            // Set created date if not already set
            if (sessionLog.getCreatedDate() == null) {
                sessionLog.setCreatedDate(new Date());
            }

            // Convert sessionLog to JSON string
            String sessionLogJson = objectMapper.writeValueAsString(sessionLog);

            // Parse to Document
            Document sessionLogDoc = Document.parse(sessionLogJson);

            // Remove the _id field if it exists and is null, let MongoDB generate it
            if (sessionLogDoc.containsKey("_id") && sessionLogDoc.get("_id") == null) {
                sessionLogDoc.remove("_id");
            }

            // Insert the document
            collection.insertOne(sessionLogDoc);

            logger.info("Session log created successfully for user: " + sessionLog.getUserId());

        } catch (Exception e) {
            logger.severe("Exception in " + methodName + ": " + e.getMessage());
            throw new RuntimeException("Failed to create session log: " + e.getMessage());
        }
    }

    @Override
    public List<UserSessionLog> getUserSessionLogs(String userId) {
        String methodName = "getUserSessionLogs";
        logger.info("Entering into " + methodName);

        List<UserSessionLog> sessionLogs = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try (MongoClient client = MongoDBConnection.connect()) {
            // Get the database
            MongoDatabase database = MongoDBConnection.getDatabase(client, SeeMeeConstants.SeeMee);

            // Get the collection
            MongoCollection<Document> collection = database.getCollection("userSessionLogs");

            // Query for logs with the given userId
            Document filter = new Document("userId", userId);
            MongoCursor<Document> cursor = collection.find(filter)
                    .sort(new Document("loginTime", -1)) // Sort by login time descending
                    .iterator();

            while (cursor.hasNext()) {
                Document doc = cursor.next();
                UserSessionLog sessionLog = objectMapper.readValue(doc.toJson(), UserSessionLog.class);
                sessionLogs.add(sessionLog);
            }

            logger.info("Retrieved " + sessionLogs.size() + " session logs for user: " + userId);

        } catch (Exception e) {
            logger.severe("Exception in " + methodName + ": " + e.getMessage());
        }

        return sessionLogs;
    }

    @Override
    public List<UserSessionLog> getSessionLogsByDateRange(String startDate, String endDate) {
        String methodName = "getSessionLogsByDateRange";
        logger.info("Entering into " + methodName);

        List<UserSessionLog> sessionLogs = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try (MongoClient client = MongoDBConnection.connect()) {
            // Get the database
            MongoDatabase database = MongoDBConnection.getDatabase(client, SeeMeeConstants.SeeMee);

            // Get the collection
            MongoCollection<Document> collection = database.getCollection("userSessionLogs");

            // Parse date strings
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);

            // Query for logs within date range
            Document filter = new Document("loginTime",
                    new Document("$gte", start).append("$lte", end));

            MongoCursor<Document> cursor = collection.find(filter)
                    .sort(new Document("loginTime", -1))
                    .iterator();

            while (cursor.hasNext()) {
                Document doc = cursor.next();
                UserSessionLog sessionLog = objectMapper.readValue(doc.toJson(), UserSessionLog.class);
                sessionLogs.add(sessionLog);
            }

            logger.info("Retrieved " + sessionLogs.size() + " session logs for date range: " + startDate + " to "
                    + endDate);

        } catch (Exception e) {
            logger.severe("Exception in " + methodName + ": " + e.getMessage());
        }

        return sessionLogs;
    }

    @Override
    public UserSessionLog getActiveSession(String userId) {
        String methodName = "getActiveSession";
        logger.info("Entering into " + methodName);

        UserSessionLog sessionLog = null;
        ObjectMapper objectMapper = new ObjectMapper();

        try (MongoClient client = MongoDBConnection.connect()) {
            // Get the database
            MongoDatabase database = MongoDBConnection.getDatabase(client, SeeMeeConstants.SeeMee);

            // Get the collection
            MongoCollection<Document> collection = database.getCollection("userSessionLogs");

            // Query for active session (login without logout)
            Document filter = new Document("userId", userId)
                    .append("action", "LOGIN")
                    .append("logoutTime", null);

            MongoCursor<Document> cursor = collection.find(filter)
                    .sort(new Document("loginTime", -1))
                    .limit(1)
                    .iterator();

            if (cursor.hasNext()) {
                Document doc = cursor.next();
                sessionLog = objectMapper.readValue(doc.toJson(), UserSessionLog.class);
            }

            logger.info("Retrieved active session for user: " + userId);

        } catch (Exception e) {
            logger.severe("Exception in " + methodName + ": " + e.getMessage());
        }

        return sessionLog;
    }

    @Override
    public void updateLogoutTime(String sessionId) {
        String methodName = "updateLogoutTime";
        logger.info("Entering into " + methodName);

        try (MongoClient client = MongoDBConnection.connect()) {
            // Get the database
            MongoDatabase database = MongoDBConnection.getDatabase(client, SeeMeeConstants.SeeMee);

            // Get the collection
            MongoCollection<Document> collection = database.getCollection("userSessionLogs");

            Date logoutTime = new Date();

            // Update the session log with logout time
            Document filter = new Document("sessionId", sessionId);
            Document update = new Document("$set",
                    new Document("logoutTime", logoutTime)
                            .append("action", "LOGOUT"));

            var result = collection.updateOne(filter, update);

            if (result.getModifiedCount() > 0) {
                logger.info("Logout time updated successfully for session: " + sessionId);
            } else {
                logger.warning("No session found to update logout time for session: " + sessionId);
            }

        } catch (Exception e) {
            logger.severe("Exception in " + methodName + ": " + e.getMessage());
            throw new RuntimeException("Failed to update logout time: " + e.getMessage());
        }
    }

    @Override
    public List<UserSessionLog> getFailedLoginAttempts(String email) {
        String methodName = "getFailedLoginAttempts";
        logger.info("Entering into " + methodName);

        List<UserSessionLog> sessionLogs = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try (MongoClient client = MongoDBConnection.connect()) {
            // Get the database
            MongoDatabase database = MongoDBConnection.getDatabase(client, SeeMeeConstants.SeeMee);

            // Get the collection
            MongoCollection<Document> collection = database.getCollection("userSessionLogs");

            // Query for failed login attempts
            Document filter = new Document("email", email)
                    .append("loginStatus", "FAILED");

            MongoCursor<Document> cursor = collection.find(filter)
                    .sort(new Document("loginTime", -1))
                    .iterator();

            while (cursor.hasNext()) {
                Document doc = cursor.next();
                UserSessionLog sessionLog = objectMapper.readValue(doc.toJson(), UserSessionLog.class);
                sessionLogs.add(sessionLog);
            }

            logger.info("Retrieved " + sessionLogs.size() + " failed login attempts for email: " + email);

        } catch (Exception e) {
            logger.severe("Exception in " + methodName + ": " + e.getMessage());
        }

        return sessionLogs;
    }
}
