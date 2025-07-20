package com.seemee.userservice.dto;

import lombok.Data;

@Data
public class ShopDTO {
    private Long id;
    private String name;
    private AddressDTO address;
    private double rating;
    private double pricePerItem;
    private boolean available;
    private UserDTO owner;
}