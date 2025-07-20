package com.seemee.userservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Address {
    @JsonProperty("index")
    private Integer index;
    @JsonProperty("houseNo")
    private String houseNo;
    @JsonProperty("apartmentName")
    private String apartmentName;
    @JsonProperty("landmark")
    private String landmark;
    @JsonProperty("street")
    private String street;
    @JsonProperty("city")
    private String city;
    @JsonProperty("state")
    private String state;
    @JsonProperty("zipCode")
    private String zipCode;
    @JsonProperty("country")
    private String country;
    @JsonProperty("addressType")
    private String addressType; // HOME, WORK, OTHER
}
