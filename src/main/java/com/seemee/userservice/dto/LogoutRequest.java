package com.seemee.userservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LogoutRequest {
    @JsonProperty("sessionId")
    private String sessionId;

    @JsonProperty("userId")
    private String userId;
}
