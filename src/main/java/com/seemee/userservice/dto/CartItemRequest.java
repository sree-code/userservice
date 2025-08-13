package com.seemee.userservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CartItemRequest {
    @JsonProperty("userId")
    private String userId;

    @JsonProperty("itemId")
    private String itemId;

    @JsonProperty("itemType")
    private String itemType;

    @JsonProperty("itemDescription")
    private String itemDescription;

    @JsonProperty("itemPrice")
    private String itemPrice;

    @JsonProperty("itemQuantity")
    private String itemQuantity;
}
