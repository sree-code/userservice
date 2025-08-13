package com.seemee.userservice.service.impl;

import com.seemee.userservice.constants.SeeMeeConstants;
import com.seemee.userservice.dto.AuthenticateUser;
import com.seemee.userservice.dto.LoginRequest;
import com.seemee.userservice.dto.LoginResponse;
import com.seemee.userservice.dto.LogoutRequest;
import com.seemee.userservice.model.User;
import com.seemee.userservice.repository.UserRepository;
import com.seemee.userservice.service.EmailService;
import com.seemee.userservice.service.UserService;
import com.seemee.userservice.service.UserSessionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class UserServiceImpl implements UserService {
    Logger logger = Logger.getLogger(UserServiceImpl.class.getName());

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    UserSessionLogService userSessionLogService;

    @Override
    public User getUserProfile(String email) {
        return userRepository.getUserProfile(email);
    }

    @Override
    public void createUserProfile(User user) {
        String methodName = "createUserProfile";
        logger.info("Entering into " + methodName);
        logger.info("Creating user profile for email: " + user.getEmail());

        // Create user profile in database
        userRepository.createUserProfile(user);

        // Send welcome email
        try {
            emailService.sendWelcomeEmail(user);
            logger.info("Welcome email triggered for user: " + user.getEmail());
        } catch (Exception e) {
            logger.warning("Failed to send welcome email to " + user.getEmail() + ": " + e.getMessage());
            // Don't fail user creation if email fails
        }

        logger.info("Exiting from " + methodName);
    }

    @Override
    public LoginResponse authenticateUser(AuthenticateUser authenticateUser) {
        String methodName = "authenticateUser";
        logger.info("Entering into " + methodName);
        logger.info("Request:- " + authenticateUser);
        User user = userRepository.authenticateUser(authenticateUser);
        logger.info("Response:- " + user);
        LoginResponse loginResponse = new LoginResponse();
        if (BCrypt.checkpw(authenticateUser.getPassword(), user.getPassword())) {
            loginResponse.setStatus(SeeMeeConstants.SUCCESS);
            loginResponse.setUser(user);
            loginResponse.setAuthenticated(true);
            loginResponse.setUserId(user.getUserId());
            loginResponse.setRole(String.join(",", user.getRole()));
            loginResponse.setToken(user.getUserId() + user.getEmail().length());
        } else {
            loginResponse.setAuthenticated(false);
            loginResponse.setStatus(SeeMeeConstants.FAILED);
        }
        logger.info("Exiting from " + methodName);
        return loginResponse;
    }

    @Override
    public String updatePassword(AuthenticateUser authenticateUser) {
        String methodName = "updatePassword";
        logger.info("Entering into " + methodName);
        logger.info("Request:- " + authenticateUser);
        String result = userRepository.updatePassword(authenticateUser);
        logger.info("Exiting from " + methodName);
        return result;
    }

    @Override
    public String updateAddress(User user, String index) {
        return userRepository.updateAddress(user, index);
    }

    @Override
    public LoginResponse authenticateUserWithLogging(LoginRequest loginRequest) {
        String methodName = "authenticateUserWithLogging";
        logger.info("Entering into " + methodName);
        logger.info("Login attempt for email: " + loginRequest.getEmail());

        AuthenticateUser authenticateUser = new AuthenticateUser();
        authenticateUser.setEmail(loginRequest.getEmail());
        authenticateUser.setPassword(loginRequest.getPassword());

        User user = userRepository.authenticateUser(authenticateUser);
        LoginResponse loginResponse = new LoginResponse();
        String sessionId = null;
        String loginStatus = "FAILED";
        String failureReason = null;

        try {
            if (user != null && user.getEmail() != null
                    && BCrypt.checkpw(loginRequest.getPassword(), user.getPassword())) {
                loginResponse.setStatus(SeeMeeConstants.SUCCESS);
                loginResponse.setUser(user);
                loginResponse.setAuthenticated(true);
                loginResponse.setUserId(user.getUserId());
                loginResponse.setRole(String.join(",", user.getRole()));
                loginResponse.setToken(user.getUserId() + user.getEmail().length());
                loginStatus = "SUCCESS";

                // Log successful login
                sessionId = userSessionLogService.logUserLogin(
                        user.getUserId(),
                        user.getEmail(),
                        loginRequest.getIpAddress(),
                        loginRequest.getUserAgent(),
                        loginRequest.getDeviceType(),
                        loginRequest.getBrowserName(),
                        loginRequest.getOperatingSystem(),
                        loginRequest.getLocation(),
                        loginStatus,
                        null);

                loginResponse.setSessionId(sessionId);
            } else {
                loginResponse.setAuthenticated(false);
                loginResponse.setStatus(SeeMeeConstants.FAILED);
                failureReason = user == null ? "User not found" : "Invalid password";

                // Log failed login attempt
                userSessionLogService.logUserLogin(
                        user != null ? user.getUserId() : null,
                        loginRequest.getEmail(),
                        loginRequest.getIpAddress(),
                        loginRequest.getUserAgent(),
                        loginRequest.getDeviceType(),
                        loginRequest.getBrowserName(),
                        loginRequest.getOperatingSystem(),
                        loginRequest.getLocation(),
                        loginStatus,
                        failureReason);
            }
        } catch (Exception e) {
            logger.severe("Login error: " + e.getMessage());
            loginResponse.setAuthenticated(false);
            loginResponse.setStatus(SeeMeeConstants.FAILED);
            failureReason = "System error during login";

            // Log login error
            userSessionLogService.logUserLogin(
                    null,
                    loginRequest.getEmail(),
                    loginRequest.getIpAddress(),
                    loginRequest.getUserAgent(),
                    loginRequest.getDeviceType(),
                    loginRequest.getBrowserName(),
                    loginRequest.getOperatingSystem(),
                    loginRequest.getLocation(),
                    "ERROR",
                    failureReason);
        }

        logger.info("Exiting from " + methodName);
        return loginResponse;
    }

    @Override
    public void logoutUser(LogoutRequest logoutRequest) {
        String methodName = "logoutUser";
        logger.info("Entering into " + methodName);
        logger.info("Logout request for session: " + logoutRequest.getSessionId());

        try {
            userSessionLogService.logUserLogout(logoutRequest.getSessionId());
            logger.info("User logged out successfully");
        } catch (Exception e) {
            logger.severe("Error during logout: " + e.getMessage());
            // Don't throw exception to avoid disrupting logout flow
        }

        logger.info("Exiting from " + methodName);
    }

}
