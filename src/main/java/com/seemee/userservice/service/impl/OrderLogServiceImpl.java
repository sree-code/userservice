package com.seemee.userservice.service.impl;

import com.seemee.userservice.model.OrderLog;
import com.seemee.userservice.repository.OrderLogRepository;
import com.seemee.userservice.service.OrderLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Service
public class OrderLogServiceImpl implements OrderLogService {
    Logger logger = Logger.getLogger(OrderLogServiceImpl.class.getName());

    @Autowired
    OrderLogRepository orderLogRepository;

    @Override
    public void logOrderAction(String orderId, String userId, String action, String previousStatus,
            String newStatus, String description, String actionBy, String ipAddress,
            String userAgent, String additionalDetails) {
        String methodName = "logOrderAction";
        logger.info("Entering into " + methodName);

        try {
            OrderLog orderLog = new OrderLog();
            orderLog.setOrderId(orderId);
            orderLog.setUserId(userId);
            orderLog.setAction(action);
            orderLog.setPreviousStatus(previousStatus);
            orderLog.setNewStatus(newStatus);
            orderLog.setDescription(description);
            orderLog.setActionBy(actionBy);
            orderLog.setTimestamp(new Date());
            orderLog.setIpAddress(ipAddress);
            orderLog.setUserAgent(userAgent);
            orderLog.setAdditionalDetails(additionalDetails);
            orderLog.setCreatedDate(new Date());

            orderLogRepository.createOrderLog(orderLog);

            logger.info("Order action logged successfully: " + action + " for order: " + orderId);
        } catch (Exception e) {
            logger.severe("Failed to log order action: " + e.getMessage());
            // Don't throw exception to avoid disrupting main order flow
        } finally {
            logger.info("Exiting from " + methodName);
        }
    }

    @Override
    public List<OrderLog> getOrderLogs(String orderId) {
        String methodName = "getOrderLogs";
        logger.info("Entering into " + methodName);

        try {
            return orderLogRepository.getOrderLogs(orderId);
        } catch (Exception e) {
            logger.severe("Failed to get order logs: " + e.getMessage());
            throw new RuntimeException("Failed to get order logs: " + e.getMessage());
        } finally {
            logger.info("Exiting from " + methodName);
        }
    }

    @Override
    public List<OrderLog> getUserOrderLogs(String userId) {
        String methodName = "getUserOrderLogs";
        logger.info("Entering into " + methodName);

        try {
            return orderLogRepository.getUserOrderLogs(userId);
        } catch (Exception e) {
            logger.severe("Failed to get user order logs: " + e.getMessage());
            throw new RuntimeException("Failed to get user order logs: " + e.getMessage());
        } finally {
            logger.info("Exiting from " + methodName);
        }
    }

    @Override
    public List<OrderLog> getOrderLogsByDateRange(String startDate, String endDate) {
        String methodName = "getOrderLogsByDateRange";
        logger.info("Entering into " + methodName);

        try {
            return orderLogRepository.getOrderLogsByDateRange(startDate, endDate);
        } catch (Exception e) {
            logger.severe("Failed to get order logs by date range: " + e.getMessage());
            throw new RuntimeException("Failed to get order logs by date range: " + e.getMessage());
        } finally {
            logger.info("Exiting from " + methodName);
        }
    }

    @Override
    public List<OrderLog> getOrderLogsByAction(String action) {
        String methodName = "getOrderLogsByAction";
        logger.info("Entering into " + methodName);

        try {
            return orderLogRepository.getOrderLogsByAction(action);
        } catch (Exception e) {
            logger.severe("Failed to get order logs by action: " + e.getMessage());
            throw new RuntimeException("Failed to get order logs by action: " + e.getMessage());
        } finally {
            logger.info("Exiting from " + methodName);
        }
    }
}
