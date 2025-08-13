package com.seemee.userservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UpdateCartItemRequest {
    @JsonProperty("userId")
    private String userId;

    @JsonProperty("cartId")
    private String cartId;

    @JsonProperty("itemQuantity")
    private String itemQuantity;

    @JsonProperty("itemPrice")
    private String itemPrice;
}
