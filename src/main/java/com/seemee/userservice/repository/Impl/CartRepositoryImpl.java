package com.seemee.userservice.repository.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.seemee.userservice.config.MongoDBConnection;
import com.seemee.userservice.constants.SeeMeeConstants;
import com.seemee.userservice.dto.CartItemRequest;
import com.seemee.userservice.dto.CartResponse;
import com.seemee.userservice.dto.UpdateCartItemRequest;
import com.seemee.userservice.model.User;
import com.seemee.userservice.repository.CartRepository;
import org.bson.Document;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Logger;

@Repository
public class CartRepositoryImpl implements CartRepository {
    Logger logger = Logger.getLogger(CartRepositoryImpl.class.getName());

    @Override
    public String addItemToCart(CartItemRequest cartItemRequest) {
        String methodName = "addItemToCart";
        logger.info("Entering into " + methodName);

        try (MongoClient client = MongoDBConnection.connect()) {
            // Get the database
            MongoDatabase database = MongoDBConnection.getDatabase(client, SeeMeeConstants.SeeMee);

            // Get the collection
            MongoCollection<Document> collection = database.getCollection(SeeMeeConstants.user);

            // Generate unique cart ID
            String cartId = UUID.randomUUID().toString();

            // Calculate item total price
            BigDecimal itemPrice = new BigDecimal(cartItemRequest.getItemPrice());
            BigDecimal quantity = new BigDecimal(cartItemRequest.getItemQuantity());
            BigDecimal itemTotal = itemPrice.multiply(quantity);

            // Check if item already exists in cart
            Document userFilter = new Document("userId", cartItemRequest.getUserId());
            MongoCursor<Document> cursor = collection.find(userFilter).iterator();

            boolean itemExists = false;
            String existingCartId = null;

            while (cursor.hasNext()) {
                Document userDoc = cursor.next();
                @SuppressWarnings("unchecked")
                List<Document> cartItems = (List<Document>) userDoc.get("cart");

                if (cartItems != null) {
                    for (Document cartItem : cartItems) {
                        if (cartItemRequest.getItemId().equals(cartItem.getString("itemId"))) {
                            itemExists = true;
                            existingCartId = cartItem.getString("cartId");
                            break;
                        }
                    }
                }
            }

            if (itemExists) {
                // Update existing item quantity
                Document filter = new Document("userId", cartItemRequest.getUserId())
                        .append("cart.cartId", existingCartId);

                // Get current quantity and add new quantity
                cursor = collection.find(new Document("userId", cartItemRequest.getUserId())).iterator();
                BigDecimal currentQuantity = BigDecimal.ZERO;

                while (cursor.hasNext()) {
                    Document userDoc = cursor.next();
                    @SuppressWarnings("unchecked")
                    List<Document> cartItems = (List<Document>) userDoc.get("cart");

                    if (cartItems != null) {
                        for (Document cartItem : cartItems) {
                            if (existingCartId != null && existingCartId.equals(cartItem.getString("cartId"))) {
                                currentQuantity = new BigDecimal(cartItem.getString("itemQuantity"));
                                break;
                            }
                        }
                    }
                }

                BigDecimal newQuantity = currentQuantity.add(quantity);
                BigDecimal newTotal = itemPrice.multiply(newQuantity);

                Document update = new Document("$set",
                        new Document("cart.$.itemQuantity", newQuantity.toString())
                                .append("cart.$.itemTotalPrice", newTotal.toString()));

                var result = collection.updateOne(filter, update);

                if (result.getModifiedCount() > 0) {
                    logger.info("Item quantity updated in cart: " + existingCartId);
                    return "Item quantity updated in cart successfully";
                } else {
                    return "Failed to update item quantity in cart";
                }
            } else {
                // Add new item to cart
                Document cartItem = new Document()
                        .append("cartId", cartId)
                        .append("itemId", cartItemRequest.getItemId())
                        .append("itemType", cartItemRequest.getItemType())
                        .append("itemDescription", cartItemRequest.getItemDescription())
                        .append("itemPrice", cartItemRequest.getItemPrice())
                        .append("itemQuantity", cartItemRequest.getItemQuantity())
                        .append("itemTotalPrice", itemTotal.toString())
                        .append("addedDate", new Date());

                Document filter = new Document("userId", cartItemRequest.getUserId());
                Document update = new Document("$push", new Document("cart", cartItem));

                var result = collection.updateOne(filter, update);

                if (result.getModifiedCount() > 0) {
                    logger.info("Item added to cart successfully: " + cartId);
                    return cartId;
                } else {
                    throw new RuntimeException("User not found or item addition failed");
                }
            }

        } catch (Exception e) {
            logger.severe("Exception in " + methodName + ": " + e.getMessage());
            throw new RuntimeException("Failed to add item to cart: " + e.getMessage());
        }
    }

    @Override
    public String updateCartItem(UpdateCartItemRequest updateRequest) {
        String methodName = "updateCartItem";
        logger.info("Entering into " + methodName);

        try (MongoClient client = MongoDBConnection.connect()) {
            // Get the database
            MongoDatabase database = MongoDBConnection.getDatabase(client, SeeMeeConstants.SeeMee);

            // Get the collection
            MongoCollection<Document> collection = database.getCollection(SeeMeeConstants.user);

            // Calculate new total price
            BigDecimal itemPrice = new BigDecimal(updateRequest.getItemPrice());
            BigDecimal quantity = new BigDecimal(updateRequest.getItemQuantity());
            BigDecimal itemTotal = itemPrice.multiply(quantity);

            // Update cart item
            Document filter = new Document("userId", updateRequest.getUserId())
                    .append("cart.cartId", updateRequest.getCartId());
            Document update = new Document("$set",
                    new Document("cart.$.itemQuantity", updateRequest.getItemQuantity())
                            .append("cart.$.itemPrice", updateRequest.getItemPrice())
                            .append("cart.$.itemTotalPrice", itemTotal.toString())
                            .append("cart.$.updatedDate", new Date()));

            var result = collection.updateOne(filter, update);

            if (result.getModifiedCount() > 0) {
                logger.info("Cart item updated successfully: " + updateRequest.getCartId());
                return "Cart item updated successfully";
            } else {
                return "Cart item not found or update failed";
            }

        } catch (Exception e) {
            logger.severe("Exception in " + methodName + ": " + e.getMessage());
            throw new RuntimeException("Failed to update cart item: " + e.getMessage());
        }
    }

    @Override
    public String removeItemFromCart(String userId, String cartId) {
        String methodName = "removeItemFromCart";
        logger.info("Entering into " + methodName);

        try (MongoClient client = MongoDBConnection.connect()) {
            // Get the database
            MongoDatabase database = MongoDBConnection.getDatabase(client, SeeMeeConstants.SeeMee);

            // Get the collection
            MongoCollection<Document> collection = database.getCollection(SeeMeeConstants.user);

            // Remove cart item
            Document filter = new Document("userId", userId);
            Document update = new Document("$pull",
                    new Document("cart", new Document("cartId", cartId)));

            var result = collection.updateOne(filter, update);

            if (result.getModifiedCount() > 0) {
                logger.info("Item removed from cart successfully: " + cartId);
                return "Item removed from cart successfully";
            } else {
                return "Cart item not found or removal failed";
            }

        } catch (Exception e) {
            logger.severe("Exception in " + methodName + ": " + e.getMessage());
            throw new RuntimeException("Failed to remove item from cart: " + e.getMessage());
        }
    }

    @Override
    public CartResponse getUserCart(String userId) {
        String methodName = "getUserCart";
        logger.info("Entering into " + methodName);

        try (MongoClient client = MongoDBConnection.connect()) {
            // Get the database
            MongoDatabase database = MongoDBConnection.getDatabase(client, SeeMeeConstants.SeeMee);

            // Get the collection
            MongoCollection<Document> collection = database.getCollection(SeeMeeConstants.user);

            Document filter = new Document("userId", userId);
            MongoCursor<Document> cursor = collection.find(filter).iterator();

            CartResponse cartResponse = new CartResponse();
            cartResponse.setUserId(userId);
            cartResponse.setCartItems(new ArrayList<>());
            cartResponse.setTotalItems(0);
            cartResponse.setTotalPrice("0.00");

            while (cursor.hasNext()) {
                Document userDoc = cursor.next();
                @SuppressWarnings("unchecked")
                List<Document> cartItems = (List<Document>) userDoc.get("cart");

                if (cartItems != null && !cartItems.isEmpty()) {
                    cartResponse.setCartItems(new ArrayList<>(cartItems));
                    cartResponse.setTotalItems(cartItems.size());

                    // Calculate total price
                    BigDecimal totalPrice = BigDecimal.ZERO;
                    for (Document cartItem : cartItems) {
                        String itemTotalStr = cartItem.getString("itemTotalPrice");
                        if (itemTotalStr != null) {
                            totalPrice = totalPrice.add(new BigDecimal(itemTotalStr));
                        }
                    }
                    cartResponse.setTotalPrice(totalPrice.toString());
                }
            }

            cartResponse.setLastUpdated(new Date().toString());
            logger.info("Retrieved cart for user: " + userId + " with " + cartResponse.getTotalItems() + " items");
            return cartResponse;

        } catch (Exception e) {
            logger.severe("Exception in " + methodName + ": " + e.getMessage());
            throw new RuntimeException("Failed to get user cart: " + e.getMessage());
        }
    }

    @Override
    public String clearCart(String userId) {
        String methodName = "clearCart";
        logger.info("Entering into " + methodName);

        try (MongoClient client = MongoDBConnection.connect()) {
            // Get the database
            MongoDatabase database = MongoDBConnection.getDatabase(client, SeeMeeConstants.SeeMee);

            // Get the collection
            MongoCollection<Document> collection = database.getCollection(SeeMeeConstants.user);

            // Clear cart
            Document filter = new Document("userId", userId);
            Document update = new Document("$set", new Document("cart", new ArrayList<>()));

            var result = collection.updateOne(filter, update);

            if (result.getModifiedCount() > 0) {
                logger.info("Cart cleared successfully for user: " + userId);
                return "Cart cleared successfully";
            } else {
                return "User not found or cart clear failed";
            }

        } catch (Exception e) {
            logger.severe("Exception in " + methodName + ": " + e.getMessage());
            throw new RuntimeException("Failed to clear cart: " + e.getMessage());
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
}
