package com.seemee.userservice.controller;

import com.seemee.userservice.constants.SeeMeeConstants;
import com.seemee.userservice.model.OrderLog;
import com.seemee.userservice.service.OrderLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/order-logs")
public class OrderLogController {
    Logger logger = Logger.getLogger(OrderLogController.class.getName());

    @Autowired
    OrderLogService orderLogService;

    /**
     * Get all logs for a specific order
     * 
     * @param orderId Order ID
     * @return List of order logs
     */
    @CrossOrigin(origins = SeeMeeConstants.FRONTEND_LOCAL_URL)
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderLog>> getOrderLogs(@PathVariable String orderId) {
        logger.info("Getting logs for order: " + orderId);

        try {
            List<OrderLog> logs = orderLogService.getOrderLogs(orderId);
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            logger.severe("Failed to get order logs: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Get all order logs for a specific user
     * 
     * @param userId User ID
     * @return List of order logs for the user
     */
    @CrossOrigin(origins = SeeMeeConstants.FRONTEND_LOCAL_URL)
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderLog>> getUserOrderLogs(@PathVariable String userId) {
        logger.info("Getting order logs for user: " + userId);

        try {
            List<OrderLog> logs = orderLogService.getUserOrderLogs(userId);
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            logger.severe("Failed to get user order logs: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Get order logs by date range
     * 
     * @param startDate Start date (yyyy-MM-dd)
     * @param endDate   End date (yyyy-MM-dd)
     * @return List of order logs within date range
     */
    @CrossOrigin(origins = SeeMeeConstants.FRONTEND_LOCAL_URL)
    @GetMapping("/date-range")
    public ResponseEntity<List<OrderLog>> getOrderLogsByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        logger.info("Getting order logs for date range: " + startDate + " to " + endDate);

        try {
            List<OrderLog> logs = orderLogService.getOrderLogsByDateRange(startDate, endDate);
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            logger.severe("Failed to get order logs by date range: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Get order logs by action type
     * 
     * @param action Action type (CREATE, UPDATE, CANCEL, etc.)
     * @return List of order logs for the action
     */
    @CrossOrigin(origins = SeeMeeConstants.FRONTEND_LOCAL_URL)
    @GetMapping("/action/{action}")
    public ResponseEntity<List<OrderLog>> getOrderLogsByAction(@PathVariable String action) {
        logger.info("Getting order logs for action: " + action);

        try {
            List<OrderLog> logs = orderLogService.getOrderLogsByAction(action);
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            logger.severe("Failed to get order logs by action: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }
}
