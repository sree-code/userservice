package com.seemee.userservice.service.impl;


import com.seemee.userservice.constants.SeeMeeConstants;
import com.seemee.userservice.dto.AuthenticateUser;
import com.seemee.userservice.dto.LoginResponse;
import com.seemee.userservice.model.User;
import com.seemee.userservice.repository.UserRepository;
import com.seemee.userservice.service.UserService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class UserServiceImpl implements UserService {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    Logger logger = Logger.getLogger(UserServiceImpl.class.getName());

    @Autowired
    UserRepository userRepository;


    @Override
    public User getUserProfile(String email) {
        return userRepository.getUserProfile(email);
    }

    @Override
    public void createUserProfile(User user) {
        userRepository.createUserProfile(user);
    }

    @Override
    public LoginResponse authenticateUser(AuthenticateUser authenticateUser) {
        String methodName = "authenticateUser";
        logger.info("Entering into "+ methodName);
        logger.info("Request:- "+ authenticateUser);
        User user = userRepository.authenticateUser(authenticateUser);
        logger.info("Response:- "+ user);
        LoginResponse loginResponse = new LoginResponse();
        if (BCrypt.checkpw(authenticateUser.getPassword(), user.getPassword())) {
            loginResponse.setStatus(SeeMeeConstants.SUCCESS);
            loginResponse.setUser(user);
            loginResponse.setAuthenticated(true);
            loginResponse.setUserId(user.getUserId());
            loginResponse.setRole(String.join(",", user.getRole()));
            loginResponse.setToken(user.getUserId()+user.getEmail().length());
        } else {
            loginResponse.setAuthenticated(false);
            loginResponse.setStatus(SeeMeeConstants.FAILED);
        }
        logger.info("Exiting from "+ methodName);
        return loginResponse;
    }

    @Override
    public String updatePassword(AuthenticateUser authenticateUser) {
        String methodName = "updatePassword";
        logger.info("Entering into "+ methodName);
        logger.info("Request:- "+ authenticateUser);
        String result = userRepository.updatePassword(authenticateUser);
        logger.info("Exiting from "+ methodName);
        return result;
    }

    @Override
    public String updateAddress(User user, String index) {
        return userRepository.updateAddress(user, index);
    }


}
