package com.seemee.userservice.service;


import com.seemee.userservice.dto.AuthenticateUser;
import com.seemee.userservice.dto.LoginResponse;
import com.seemee.userservice.model.User;

public interface UserService {
    User getUserProfile(String email);
    void createUserProfile(User user);
    LoginResponse authenticateUser(AuthenticateUser authenticateUser);
    String updatePassword(AuthenticateUser authenticateUser);
    String updateAddress(User user, String type);
}
