package com.seemee.userservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class CartResponse {
    @JsonProperty("userId")
    private String userId;

    @JsonProperty("cartItems")
    private List<Object> cartItems;

    @JsonProperty("totalItems")
    private int totalItems;

    @JsonProperty("totalPrice")
    private String totalPrice;

    @JsonProperty("lastUpdated")
    private String lastUpdated;
}
