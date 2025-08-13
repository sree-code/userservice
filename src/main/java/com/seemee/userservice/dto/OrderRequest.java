package com.seemee.userservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class OrderRequest {
    @JsonProperty("userId")
    private String userId;

    @JsonProperty("deliveryAddress")
    private Address deliveryAddress;

    @JsonProperty("orderItems")
    private List<OrderItemRequest> orderItems;

    @JsonProperty("paymentMethod")
    private String paymentMethod; // CREDIT_CARD, UPI, BANK_ACCOUNT

    @JsonProperty("paymentId")
    private String paymentId; // Reference to the payment method
}
