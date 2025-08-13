package com.seemee.userservice.repository;

import com.seemee.userservice.model.OrderLog;
import java.util.List;

public interface OrderLogRepository {
    void createOrderLog(OrderLog orderLog);

    List<OrderLog> getOrderLogs(String orderId);

    List<OrderLog> getUserOrderLogs(String userId);

    List<OrderLog> getOrderLogsByDateRange(String startDate, String endDate);

    List<OrderLog> getOrderLogsByAction(String action);
}
