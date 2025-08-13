package com.seemee.userservice.repository;

import com.seemee.userservice.dto.OrderRequest;
import com.seemee.userservice.dto.UpdateOrderAddressRequest;
import com.seemee.userservice.model.User;
import java.util.List;

public interface OrderRepository {
    String createOrder(OrderRequest orderRequest);

    String cancelOrder(String userId, String orderId);

    List<Object> getUserOrders(String userId);

    String updateOrderAddress(String userId, String orderId, UpdateOrderAddressRequest updateRequest);

    String completeOrder(String userId, String orderId);

    User getUserById(String userId);
}
