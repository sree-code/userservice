package com.seemee.userservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UpdateOrderAddressRequest {
    @JsonProperty("newAddress")
    private Address newAddress;
}
