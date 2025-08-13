package com.seemee.userservice.repository.Impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.seemee.userservice.config.MongoDBConnection;
import com.seemee.userservice.constants.SeeMeeConstants;
import com.seemee.userservice.model.OrderLog;
import com.seemee.userservice.repository.OrderLogRepository;
import org.bson.Document;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Repository
public class OrderLogRepositoryImpl implements OrderLogRepository {
    Logger logger = Logger.getLogger(OrderLogRepositoryImpl.class.getName());

    @Override
    public void createOrderLog(OrderLog orderLog) {
        String methodName = "createOrderLog";
        logger.info("Entering into " + methodName);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        try (MongoClient client = MongoDBConnection.connect()) {
            // Get the database
            MongoDatabase database = MongoDBConnection.getDatabase(client, SeeMeeConstants.SeeMee);

            // Get the collection
            MongoCollection<Document> collection = database.getCollection("orderLogs");

            // Set created date if not already set
            if (orderLog.getCreatedDate() == null) {
                orderLog.setCreatedDate(new Date());
            }

            // Convert orderLog to JSON string
            String orderLogJson = objectMapper.writeValueAsString(orderLog);

            // Parse to Document
            Document orderLogDoc = Document.parse(orderLogJson);

            // Remove the _id field if it exists and is null, let MongoDB generate it
            if (orderLogDoc.containsKey("_id") && orderLogDoc.get("_id") == null) {
                orderLogDoc.remove("_id");
            }

            // Insert the document
            collection.insertOne(orderLogDoc);

            logger.info("Order log created successfully for order: " + orderLog.getOrderId());

        } catch (Exception e) {
            logger.severe("Exception in " + methodName + ": " + e.getMessage());
            throw new RuntimeException("Failed to create order log: " + e.getMessage());
        }
    }

    @Override
    public List<OrderLog> getOrderLogs(String orderId) {
        String methodName = "getOrderLogs";
        logger.info("Entering into " + methodName);

        List<OrderLog> orderLogs = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try (MongoClient client = MongoDBConnection.connect()) {
            // Get the database
            MongoDatabase database = MongoDBConnection.getDatabase(client, SeeMeeConstants.SeeMee);

            // Get the collection
            MongoCollection<Document> collection = database.getCollection("orderLogs");

            // Query for logs with the given orderId
            Document filter = new Document("orderId", orderId);
            MongoCursor<Document> cursor = collection.find(filter)
                    .sort(new Document("timestamp", -1)) // Sort by timestamp descending
                    .iterator();

            while (cursor.hasNext()) {
                Document doc = cursor.next();
                OrderLog orderLog = objectMapper.readValue(doc.toJson(), OrderLog.class);
                orderLogs.add(orderLog);
            }

            logger.info("Retrieved " + orderLogs.size() + " logs for order: " + orderId);

        } catch (Exception e) {
            logger.severe("Exception in " + methodName + ": " + e.getMessage());
        }

        return orderLogs;
    }

    @Override
    public List<OrderLog> getUserOrderLogs(String userId) {
        String methodName = "getUserOrderLogs";
        logger.info("Entering into " + methodName);

        List<OrderLog> orderLogs = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try (MongoClient client = MongoDBConnection.connect()) {
            // Get the database
            MongoDatabase database = MongoDBConnection.getDatabase(client, SeeMeeConstants.SeeMee);

            // Get the collection
            MongoCollection<Document> collection = database.getCollection("orderLogs");

            // Query for logs with the given userId
            Document filter = new Document("userId", userId);
            MongoCursor<Document> cursor = collection.find(filter)
                    .sort(new Document("timestamp", -1)) // Sort by timestamp descending
                    .iterator();

            while (cursor.hasNext()) {
                Document doc = cursor.next();
                OrderLog orderLog = objectMapper.readValue(doc.toJson(), OrderLog.class);
                orderLogs.add(orderLog);
            }

            logger.info("Retrieved " + orderLogs.size() + " order logs for user: " + userId);

        } catch (Exception e) {
            logger.severe("Exception in " + methodName + ": " + e.getMessage());
        }

        return orderLogs;
    }

    @Override
    public List<OrderLog> getOrderLogsByDateRange(String startDate, String endDate) {
        String methodName = "getOrderLogsByDateRange";
        logger.info("Entering into " + methodName);

        List<OrderLog> orderLogs = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try (MongoClient client = MongoDBConnection.connect()) {
            // Get the database
            MongoDatabase database = MongoDBConnection.getDatabase(client, SeeMeeConstants.SeeMee);

            // Get the collection
            MongoCollection<Document> collection = database.getCollection("orderLogs");

            // Parse date strings
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);

            // Query for logs within date range
            Document filter = new Document("timestamp",
                    new Document("$gte", start).append("$lte", end));

            MongoCursor<Document> cursor = collection.find(filter)
                    .sort(new Document("timestamp", -1))
                    .iterator();

            while (cursor.hasNext()) {
                Document doc = cursor.next();
                OrderLog orderLog = objectMapper.readValue(doc.toJson(), OrderLog.class);
                orderLogs.add(orderLog);
            }

            logger.info(
                    "Retrieved " + orderLogs.size() + " order logs for date range: " + startDate + " to " + endDate);

        } catch (Exception e) {
            logger.severe("Exception in " + methodName + ": " + e.getMessage());
        }

        return orderLogs;
    }

    @Override
    public List<OrderLog> getOrderLogsByAction(String action) {
        String methodName = "getOrderLogsByAction";
        logger.info("Entering into " + methodName);

        List<OrderLog> orderLogs = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try (MongoClient client = MongoDBConnection.connect()) {
            // Get the database
            MongoDatabase database = MongoDBConnection.getDatabase(client, SeeMeeConstants.SeeMee);

            // Get the collection
            MongoCollection<Document> collection = database.getCollection("orderLogs");

            // Query for logs with the given action
            Document filter = new Document("action", action);
            MongoCursor<Document> cursor = collection.find(filter)
                    .sort(new Document("timestamp", -1))
                    .iterator();

            while (cursor.hasNext()) {
                Document doc = cursor.next();
                OrderLog orderLog = objectMapper.readValue(doc.toJson(), OrderLog.class);
                orderLogs.add(orderLog);
            }

            logger.info("Retrieved " + orderLogs.size() + " order logs for action: " + action);

        } catch (Exception e) {
            logger.severe("Exception in " + methodName + ": " + e.getMessage());
        }

        return orderLogs;
    }
}
