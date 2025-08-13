package com.seemee.userservice.repository.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.seemee.userservice.config.MongoDBConnection;
import com.seemee.userservice.constants.SeeMeeConstants;
import com.seemee.userservice.dto.OrderRequest;
import com.seemee.userservice.dto.UpdateOrderAddressRequest;
import com.seemee.userservice.model.User;
import com.seemee.userservice.repository.OrderRepository;
import org.bson.Document;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Logger;

@Repository
public class OrderRepositoryImpl implements OrderRepository {
    Logger logger = Logger.getLogger(OrderRepositoryImpl.class.getName());

    @Override
    public String createOrder(OrderRequest orderRequest) {
        String methodName = "createOrder";
        logger.info("Entering into " + methodName);

        try (MongoClient client = MongoDBConnection.connect()) {
            // Get the database
            MongoDatabase database = MongoDBConnection.getDatabase(client, SeeMeeConstants.SeeMee);

            // Get the collection
            MongoCollection<Document> collection = database.getCollection(SeeMeeConstants.user);

            // Generate unique order ID
            String orderId = UUID.randomUUID().toString();
            Date orderDate = new Date();

            // Calculate total price
            BigDecimal totalPrice = BigDecimal.ZERO;
            List<Document> orderItems = new ArrayList<>();

            for (var item : orderRequest.getOrderItems()) {
                BigDecimal itemPrice = new BigDecimal(item.getItemPrice());
                BigDecimal quantity = new BigDecimal(item.getItemQuantity());
                BigDecimal itemTotal = itemPrice.multiply(quantity);
                totalPrice = totalPrice.add(itemTotal);

                Document orderItem = new Document()
                        .append("itemId", item.getItemId())
                        .append("itemType", item.getItemType())
                        .append("itemDescription", item.getItemDescription())
                        .append("itemPrice", item.getItemPrice())
                        .append("itemQuantity", item.getItemQuantity())
                        .append("itemTotalPrice", itemTotal.toString());

                orderItems.add(orderItem);
            }

            // Create order document
            Document orderDoc = new Document()
                    .append("orderId", orderId)
                    .append("orderDate", orderDate)
                    .append("orderStatus", "IN_PROGRESS")
                    .append("orderTotalPrice", totalPrice.toString())
                    .append("deliveryAddress", convertAddressToDocument(orderRequest.getDeliveryAddress()))
                    .append("paymentMethod", orderRequest.getPaymentMethod())
                    .append("paymentId", orderRequest.getPaymentId())
                    .append("orderItems", orderItems);

            // Update user document to add the order
            Document filter = new Document("userId", orderRequest.getUserId());
            Document update = new Document("$push", new Document("orders", orderDoc));

            var result = collection.updateOne(filter, update);

            if (result.getModifiedCount() > 0) {
                logger.info("Order created successfully with ID: " + orderId);
                return orderId;
            } else {
                throw new RuntimeException("User not found or order creation failed");
            }

        } catch (Exception e) {
            logger.severe("Exception in " + methodName + ": " + e.getMessage());
            throw new RuntimeException("Failed to create order: " + e.getMessage());
        }
    }

    @Override
    public String cancelOrder(String userId, String orderId) {
        String methodName = "cancelOrder";
        logger.info("Entering into " + methodName);

        try (MongoClient client = MongoDBConnection.connect()) {
            // Get the database
            MongoDatabase database = MongoDBConnection.getDatabase(client, SeeMeeConstants.SeeMee);

            // Get the collection
            MongoCollection<Document> collection = database.getCollection(SeeMeeConstants.user);

            // Check if order exists and is cancellable
            Document userFilter = new Document("userId", userId);
            MongoCursor<Document> cursor = collection.find(userFilter).iterator();

            boolean orderFound = false;
            boolean canCancel = false;

            while (cursor.hasNext()) {
                Document userDoc = cursor.next();
                @SuppressWarnings("unchecked")
                List<Document> orders = (List<Document>) userDoc.get("orders");

                if (orders != null) {
                    for (Document order : orders) {
                        if (orderId.equals(order.getString("orderId"))) {
                            orderFound = true;
                            String status = order.getString("orderStatus");
                            canCancel = "IN_PROGRESS".equals(status) || "PENDING".equals(status);
                            break;
                        }
                    }
                }
            }

            if (!orderFound) {
                return "Order not found";
            }

            if (!canCancel) {
                return "Order cannot be cancelled. Order is not in a cancellable state.";
            }

            // Update order status to CANCELLED
            Document filter = new Document("userId", userId)
                    .append("orders.orderId", orderId);
            Document update = new Document("$set", new Document("orders.$.orderStatus", "CANCELLED")
                    .append("orders.$.cancelledDate", new Date()));

            var result = collection.updateOne(filter, update);

            if (result.getModifiedCount() > 0) {
                logger.info("Order cancelled successfully: " + orderId);
                return "Order cancelled successfully";
            } else {
                return "Failed to cancel order";
            }

        } catch (Exception e) {
            logger.severe("Exception in " + methodName + ": " + e.getMessage());
            throw new RuntimeException("Failed to cancel order: " + e.getMessage());
        }
    }

    @Override
    public List<Object> getUserOrders(String userId) {
        String methodName = "getUserOrders";
        logger.info("Entering into " + methodName);

        try (MongoClient client = MongoDBConnection.connect()) {
            // Get the database
            MongoDatabase database = MongoDBConnection.getDatabase(client, SeeMeeConstants.SeeMee);

            // Get the collection
            MongoCollection<Document> collection = database.getCollection(SeeMeeConstants.user);

            Document filter = new Document("userId", userId);
            MongoCursor<Document> cursor = collection.find(filter).iterator();

            List<Object> orders = new ArrayList<>();

            while (cursor.hasNext()) {
                Document userDoc = cursor.next();
                @SuppressWarnings("unchecked")
                List<Document> userOrders = (List<Document>) userDoc.get("orders");

                if (userOrders != null) {
                    // Sort orders by order date (newest first)
                    userOrders.sort((o1, o2) -> {
                        Date date1 = o1.getDate("orderDate");
                        Date date2 = o2.getDate("orderDate");
                        return date2.compareTo(date1);
                    });

                    orders.addAll(userOrders);
                }
            }

            logger.info("Retrieved " + orders.size() + " orders for user: " + userId);
            return orders;

        } catch (Exception e) {
            logger.severe("Exception in " + methodName + ": " + e.getMessage());
            throw new RuntimeException("Failed to get orders: " + e.getMessage());
        }
    }

    @Override
    public String updateOrderAddress(String userId, String orderId, UpdateOrderAddressRequest updateRequest) {
        String methodName = "updateOrderAddress";
        logger.info("Entering into " + methodName);

        try (MongoClient client = MongoDBConnection.connect()) {
            // Get the database
            MongoDatabase database = MongoDBConnection.getDatabase(client, SeeMeeConstants.SeeMee);

            // Get the collection
            MongoCollection<Document> collection = database.getCollection(SeeMeeConstants.user);

            // Check if order exists and is in progress
            Document userFilter = new Document("userId", userId);
            MongoCursor<Document> cursor = collection.find(userFilter).iterator();

            boolean orderFound = false;
            boolean canUpdateAddress = false;

            while (cursor.hasNext()) {
                Document userDoc = cursor.next();
                @SuppressWarnings("unchecked")
                List<Document> orders = (List<Document>) userDoc.get("orders");

                if (orders != null) {
                    for (Document order : orders) {
                        if (orderId.equals(order.getString("orderId"))) {
                            orderFound = true;
                            String status = order.getString("orderStatus");
                            canUpdateAddress = "IN_PROGRESS".equals(status);
                            break;
                        }
                    }
                }
            }

            if (!orderFound) {
                return "Order not found";
            }

            if (!canUpdateAddress) {
                return "Address can only be updated for orders in progress";
            }

            // Update order delivery address
            Document filter = new Document("userId", userId)
                    .append("orders.orderId", orderId);
            Document update = new Document("$set",
                    new Document("orders.$.deliveryAddress", convertAddressToDocument(updateRequest.getNewAddress()))
                            .append("orders.$.addressUpdatedDate", new Date()));

            var result = collection.updateOne(filter, update);

            if (result.getModifiedCount() > 0) {
                logger.info("Order address updated successfully: " + orderId);
                return "Order address updated successfully";
            } else {
                return "Failed to update order address";
            }

        } catch (Exception e) {
            logger.severe("Exception in " + methodName + ": " + e.getMessage());
            throw new RuntimeException("Failed to update order address: " + e.getMessage());
        }
    }

    @Override
    public User getUserById(String userId) {
        String methodName = "getUserById";
        logger.info("Entering into " + methodName);

        User user = new User();
        ObjectMapper objectMapper = new ObjectMapper();

        try (MongoClient client = MongoDBConnection.connect()) {
            // Get the database
            MongoDatabase database = MongoDBConnection.getDatabase(client, SeeMeeConstants.SeeMee);

            // Get the collection
            MongoCollection<Document> collection = database.getCollection(SeeMeeConstants.user);

            // Query for user with the given userId
            MongoCursor<Document> cursor = collection.find(
                    new Document("userId", userId)).iterator();

            while (cursor.hasNext()) {
                Document doc = cursor.next();
                user = objectMapper.readValue(doc.toJson(), User.class);
            }
        } catch (Exception e) {
            logger.severe("Exception in " + methodName + ": " + e.getMessage());
        }

        return user;
    }

    // Helper method to convert Address DTO to MongoDB Document
    private Document convertAddressToDocument(com.seemee.userservice.dto.Address address) {
        if (address == null) {
            return null;
        }

        return new Document()
                .append("index", address.getIndex())
                .append("houseNo", address.getHouseNo())
                .append("apartmentName", address.getApartmentName())
                .append("landmark", address.getLandmark())
                .append("street", address.getStreet())
                .append("city", address.getCity())
                .append("state", address.getState())
                .append("zipCode", address.getZipCode())
                .append("country", address.getCountry())
                .append("addressType", address.getAddressType());
    }

    @Override
    public String completeOrder(String userId, String orderId) {
        String methodName = "completeOrder";
        logger.info("Entering into " + methodName);

        try (MongoClient client = MongoDBConnection.connect()) {
            // Get the database
            MongoDatabase database = MongoDBConnection.getDatabase(client, SeeMeeConstants.SeeMee);

            // Get the collection
            MongoCollection<Document> collection = database.getCollection(SeeMeeConstants.user);

            // Check if order exists and is completable
            Document userFilter = new Document("userId", userId);
            MongoCursor<Document> cursor = collection.find(userFilter).iterator();

            boolean orderFound = false;
            boolean canComplete = false;

            while (cursor.hasNext()) {
                Document userDoc = cursor.next();
                @SuppressWarnings("unchecked")
                List<Document> orders = (List<Document>) userDoc.get("orders");

                if (orders != null) {
                    for (Document order : orders) {
                        if (orderId.equals(order.getString("orderId"))) {
                            orderFound = true;
                            String status = order.getString("orderStatus");
                            canComplete = "IN_PROGRESS".equals(status);
                            break;
                        }
                    }
                }
            }

            if (!orderFound) {
                return "Order not found";
            }

            if (!canComplete) {
                return "Order cannot be completed. Order is not in progress.";
            }

            // Update order status to COMPLETED
            Document filter = new Document("userId", userId)
                    .append("orders.orderId", orderId);
            Document update = new Document("$set", new Document("orders.$.orderStatus", "COMPLETED")
                    .append("orders.$.completedDate", new Date()));

            var result = collection.updateOne(filter, update);

            if (result.getModifiedCount() > 0) {
                logger.info("Order completed successfully: " + orderId);
                return "Order completed successfully";
            } else {
                return "Failed to complete order";
            }

        } catch (Exception e) {
            logger.severe("Exception in " + methodName + ": " + e.getMessage());
            throw new RuntimeException("Failed to complete order: " + e.getMessage());
        }
    }
}
