package com.seemee.userservice.service;

import com.seemee.userservice.dto.OrderRequest;
import com.seemee.userservice.dto.UpdateOrderAddressRequest;
import java.util.List;

public interface OrderService {
    String createOrder(OrderRequest orderRequest);

    String cancelOrder(String userId, String orderId);

    List<Object> getUserOrders(String userId);

    String updateOrderAddress(String userId, String orderId, UpdateOrderAddressRequest updateRequest);

    String completeOrder(String userId, String orderId);
}
