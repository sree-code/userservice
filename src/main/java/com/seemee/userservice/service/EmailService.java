package com.seemee.userservice.service;

import com.seemee.userservice.model.User;

public interface EmailService {
    void sendWelcomeEmail(User user);
}
