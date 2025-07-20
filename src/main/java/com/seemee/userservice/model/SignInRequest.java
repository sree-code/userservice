package com.seemee.userservice.model;

import lombok.Data;

@Data
public class SignInRequest {
    private String email;
    private String password;

    // Getters and setters
}
