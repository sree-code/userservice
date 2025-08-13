package com.seemee.userservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginRequest {
    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;

    @JsonProperty("ipAddress")
    private String ipAddress;

    @JsonProperty("userAgent")
    private String userAgent;

    @JsonProperty("deviceType")
    private String deviceType;

    @JsonProperty("browserName")
    private String browserName;

    @JsonProperty("operatingSystem")
    private String operatingSystem;

    @JsonProperty("location")
    private String location;
}
