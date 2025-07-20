package com.seemee.userservice.repository;


import com.seemee.userservice.dto.AuthenticateUser;
import com.seemee.userservice.model.User;

public interface UserRepository {
    User getUserProfile(String email);
    void createUserProfile(User user);
    User authenticateUser(AuthenticateUser authenticateUser);
    String updatePassword(AuthenticateUser authenticateUser);
    String updateAddress(User user, String type);
}
