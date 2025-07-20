package com.seemee.userservice.dto;

import com.seemee.userservice.model.User;
import lombok.Data;

@Data
public class LoginResponse {
    String status;
    String message;
    boolean authenticated;
    String userId;
    String role;
    User user;
    String token;
}
