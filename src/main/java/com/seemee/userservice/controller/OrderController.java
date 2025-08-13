package com.seemee.userservice.controller;

import com.seemee.userservice.constants.SeeMeeConstants;
import com.seemee.userservice.dto.OrderRequest;
import com.seemee.userservice.dto.UpdateOrderAddressRequest;
import com.seemee.userservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    Logger logger = Logger.getLogger(OrderController.class.getName());

    @Autowired
    OrderService orderService;

    /**
     * Create a new order
     * 
     * @param orderRequest Order details including items, delivery address, and
     *                     payment info
     * @return Order ID if successful
     */
    @CrossOrigin(origins = SeeMeeConstants.FRONTEND_LOCAL_URL)
    @PostMapping("/create")
    public ResponseEntity<String> createOrder(@RequestBody OrderRequest orderRequest) {
        logger.info("Creating order for user: " + orderRequest.getUserId());

        try {
            String orderId = orderService.createOrder(orderRequest);
            return ResponseEntity.ok("Order created successfully with ID: " + orderId);
        } catch (Exception e) {
            logger.severe("Failed to create order: " + e.getMessage());
            return ResponseEntity.badRequest().body("Failed to create order: " + e.getMessage());
        }
    }

    /**
     * Cancel an existing order
     * 
     * @param userId  User ID
     * @param orderId Order ID to cancel
     * @return Success/failure message
     */
    @CrossOrigin(origins = SeeMeeConstants.FRONTEND_LOCAL_URL)
    @PutMapping("/cancel/{userId}/{orderId}")
    public ResponseEntity<String> cancelOrder(@PathVariable String userId, @PathVariable String orderId) {
        logger.info("Cancelling order: " + orderId + " for user: " + userId);

        try {
            String result = orderService.cancelOrder(userId, orderId);

            if (result.contains("successfully")) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.badRequest().body(result);
            }
        } catch (Exception e) {
            logger.severe("Failed to cancel order: " + e.getMessage());
            return ResponseEntity.badRequest().body("Failed to cancel order: " + e.getMessage());
        }
    }

    /**
     * Get all orders for a user
     * 
     * @param userId User ID
     * @return List of orders for the user
     */
    @CrossOrigin(origins = SeeMeeConstants.FRONTEND_LOCAL_URL)
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Object>> getUserOrders(@PathVariable String userId) {
        logger.info("Getting orders for user: " + userId);

        try {
            List<Object> orders = orderService.getUserOrders(userId);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            logger.severe("Failed to get orders: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Get a specific order by ID
     * 
     * @param userId  User ID
     * @param orderId Order ID
     * @return Order details
     */
    @CrossOrigin(origins = SeeMeeConstants.FRONTEND_LOCAL_URL)
    @GetMapping("/user/{userId}/order/{orderId}")
    public ResponseEntity<Object> getOrderById(@PathVariable String userId, @PathVariable String orderId) {
        logger.info("Getting order: " + orderId + " for user: " + userId);

        try {
            List<Object> orders = orderService.getUserOrders(userId);

            // Find the specific order
            for (Object orderObj : orders) {
                if (orderObj instanceof org.bson.Document) {
                    org.bson.Document order = (org.bson.Document) orderObj;
                    if (orderId.equals(order.getString("orderId"))) {
                        return ResponseEntity.ok(order);
                    }
                }
            }

            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.severe("Failed to get order: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Update order delivery address (only allowed for orders in progress)
     * 
     * @param userId        User ID
     * @param orderId       Order ID
     * @param updateRequest New address details
     * @return Success/failure message
     */
    @CrossOrigin(origins = SeeMeeConstants.FRONTEND_LOCAL_URL)
    @PutMapping("/updateAddress/{userId}/{orderId}")
    public ResponseEntity<String> updateOrderAddress(
            @PathVariable String userId,
            @PathVariable String orderId,
            @RequestBody UpdateOrderAddressRequest updateRequest) {

        logger.info("Updating address for order: " + orderId + " user: " + userId);

        try {
            String result = orderService.updateOrderAddress(userId, orderId, updateRequest);

            if (result.contains("successfully")) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.badRequest().body(result);
            }
        } catch (Exception e) {
            logger.severe("Failed to update order address: " + e.getMessage());
            return ResponseEntity.badRequest().body("Failed to update order address: " + e.getMessage());
        }
    }

    /**
     * Complete an order (vendor action)
     * 
     * @param userId  User ID
     * @param orderId Order ID to complete
     * @return Success/failure message
     */
    @CrossOrigin(origins = SeeMeeConstants.FRONTEND_LOCAL_URL)
    @PutMapping("/complete/{userId}/{orderId}")
    public ResponseEntity<String> completeOrder(@PathVariable String userId, @PathVariable String orderId) {
        logger.info("Completing order: " + orderId + " for user: " + userId);

        try {
            String result = orderService.completeOrder(userId, orderId);

            if (result.contains("successfully")) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.badRequest().body(result);
            }
        } catch (Exception e) {
            logger.severe("Failed to complete order: " + e.getMessage());
            return ResponseEntity.badRequest().body("Failed to complete order: " + e.getMessage());
        }
    }
}
