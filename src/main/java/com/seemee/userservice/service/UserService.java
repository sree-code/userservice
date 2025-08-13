package com.seemee.userservice.service;

import com.seemee.userservice.dto.AuthenticateUser;
import com.seemee.userservice.dto.LoginRequest;
import com.seemee.userservice.dto.LoginResponse;
import com.seemee.userservice.dto.LogoutRequest;
import com.seemee.userservice.model.User;

public interface UserService {
    User getUserProfile(String email);

    void createUserProfile(User user);

    LoginResponse authenticateUser(AuthenticateUser authenticateUser);

    LoginResponse authenticateUserWithLogging(LoginRequest loginRequest);

    void logoutUser(LogoutRequest logoutRequest);

    String updatePassword(AuthenticateUser authenticateUser);

    String updateAddress(User user, String type);
}
