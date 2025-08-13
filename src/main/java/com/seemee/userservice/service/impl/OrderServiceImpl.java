package com.seemee.userservice.service.impl;

import com.seemee.userservice.dto.OrderRequest;
import com.seemee.userservice.dto.UpdateOrderAddressRequest;
import com.seemee.userservice.model.User;
import com.seemee.userservice.repository.OrderRepository;
import com.seemee.userservice.service.OrderLogService;
import com.seemee.userservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class OrderServiceImpl implements OrderService {
    Logger logger = Logger.getLogger(OrderServiceImpl.class.getName());

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderLogService orderLogService;

    @Override
    public String createOrder(OrderRequest orderRequest) {
        String methodName = "createOrder";
        logger.info("Entering into " + methodName);
        logger.info("Creating order for user: " + orderRequest.getUserId());

        try {
            // Validate user has address in DB or provided in request
            if (orderRequest.getDeliveryAddress() == null) {
                // Check if user has address in database
                User user = orderRepository.getUserById(orderRequest.getUserId());
                if (user == null || user.getAddress() == null || user.getAddress().isEmpty()) {
                    String errorMsg = "Please add delivery address to proceed with order";
                    logger.warning("Order creation failed - No address found for user: " + orderRequest.getUserId());

                    // Log order creation failure
                    orderLogService.logOrderAction(
                            null, // orderId is null since order wasn't created
                            orderRequest.getUserId(),
                            "ORDER_CREATION_FAILED",
                            null,
                            "FAILED",
                            errorMsg,
                            "SYSTEM",
                            null,
                            null,
                            "Address validation failed");

                    return errorMsg;
                }
                // Use first address from user's saved addresses if no delivery address provided
                orderRequest.setDeliveryAddress(user.getAddress().get(0));
            }

            String orderId = orderRepository.createOrder(orderRequest);

            // Log successful order creation
            orderLogService.logOrderAction(
                    orderId,
                    orderRequest.getUserId(),
                    "ORDER_CREATED",
                    null,
                    "IN_PROGRESS",
                    "Order created successfully",
                    "USER",
                    null,
                    null,
                    "Delivery to: " + orderRequest.getDeliveryAddress().getCity() + ", "
                            + orderRequest.getDeliveryAddress().getState());

            logger.info("Order created successfully for user: " + orderRequest.getUserId() + " with ID: " + orderId);
            return orderId;
        } catch (Exception e) {
            logger.severe(
                    "Failed to create order for user: " + orderRequest.getUserId() + ". Error: " + e.getMessage());

            // Log order creation error
            orderLogService.logOrderAction(
                    null,
                    orderRequest.getUserId(),
                    "ORDER_CREATION_ERROR",
                    null,
                    "FAILED",
                    "System error during order creation: " + e.getMessage(),
                    "SYSTEM",
                    null,
                    null,
                    "Exception: " + e.getClass().getSimpleName());

            throw new RuntimeException("Failed to create order: " + e.getMessage());
        } finally {
            logger.info("Exiting from " + methodName);
        }
    }

    @Override
    public String cancelOrder(String userId, String orderId) {
        String methodName = "cancelOrder";
        logger.info("Entering into " + methodName);
        logger.info("Cancelling order: " + orderId + " for user: " + userId);

        try {
            String result = orderRepository.cancelOrder(userId, orderId);
            logger.info("Order cancelled successfully: " + orderId);
            return result;
        } catch (Exception e) {
            logger.severe("Failed to cancel order: " + orderId + ". Error: " + e.getMessage());
            throw new RuntimeException("Failed to cancel order: " + e.getMessage());
        } finally {
            logger.info("Exiting from " + methodName);
        }
    }

    @Override
    public List<Object> getUserOrders(String userId) {
        String methodName = "getUserOrders";
        logger.info("Entering into " + methodName);
        logger.info("Getting orders for user: " + userId);

        try {
            List<Object> orders = orderRepository.getUserOrders(userId);
            logger.info("Retrieved " + orders.size() + " orders for user: " + userId);
            return orders;
        } catch (Exception e) {
            logger.severe("Failed to get orders for user: " + userId + ". Error: " + e.getMessage());
            throw new RuntimeException("Failed to get orders: " + e.getMessage());
        } finally {
            logger.info("Exiting from " + methodName);
        }
    }

    @Override
    public String updateOrderAddress(String userId, String orderId, UpdateOrderAddressRequest updateRequest) {
        String methodName = "updateOrderAddress";
        logger.info("Entering into " + methodName);
        logger.info("Updating address for order: " + orderId + " user: " + userId);

        try {
            String result = orderRepository.updateOrderAddress(userId, orderId, updateRequest);
            logger.info("Order address updated successfully: " + orderId);
            return result;
        } catch (Exception e) {
            logger.severe("Failed to update order address: " + orderId + ". Error: " + e.getMessage());
            throw new RuntimeException("Failed to update order address: " + e.getMessage());
        } finally {
            logger.info("Exiting from " + methodName);
        }
    }

    @Override
    public String completeOrder(String userId, String orderId) {
        String methodName = "completeOrder";
        logger.info("Entering into " + methodName);
        logger.info("Completing order: " + orderId + " for user: " + userId);

        try {
            String result = orderRepository.completeOrder(userId, orderId);

            // Log order completion
            orderLogService.logOrderAction(
                    orderId,
                    userId,
                    "ORDER_COMPLETED",
                    "IN_PROGRESS",
                    "COMPLETED",
                    "Order completed successfully by vendor",
                    "VENDOR",
                    null,
                    null,
                    "Order fulfilled and delivered");

            logger.info("Order completed successfully: " + orderId);
            return result;
        } catch (Exception e) {
            logger.severe("Failed to complete order: " + orderId + ". Error: " + e.getMessage());

            // Log order completion error
            orderLogService.logOrderAction(
                    orderId,
                    userId,
                    "ORDER_COMPLETION_ERROR",
                    "IN_PROGRESS",
                    "ERROR",
                    "Failed to complete order: " + e.getMessage(),
                    "SYSTEM",
                    null,
                    null,
                    "Exception: " + e.getClass().getSimpleName());

            throw new RuntimeException("Failed to complete order: " + e.getMessage());
        } finally {
            logger.info("Exiting from " + methodName);
        }
    }
}
