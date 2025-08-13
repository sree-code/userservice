package com.seemee.userservice.service;

import com.seemee.userservice.model.OrderLog;
import java.util.List;

public interface OrderLogService {
    void logOrderAction(String orderId, String userId, String action, String previousStatus,
            String newStatus, String description, String actionBy, String ipAddress,
            String userAgent, String additionalDetails);

    List<OrderLog> getOrderLogs(String orderId);

    List<OrderLog> getUserOrderLogs(String userId);

    List<OrderLog> getOrderLogsByDateRange(String startDate, String endDate);

    List<OrderLog> getOrderLogsByAction(String action);
}
