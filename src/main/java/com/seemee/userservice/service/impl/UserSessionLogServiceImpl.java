package com.seemee.userservice.service.impl;

import com.seemee.userservice.model.UserSessionLog;
import com.seemee.userservice.repository.UserSessionLogRepository;
import com.seemee.userservice.service.UserSessionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class UserSessionLogServiceImpl implements UserSessionLogService {
    Logger logger = Logger.getLogger(UserSessionLogServiceImpl.class.getName());

    @Autowired
    UserSessionLogRepository userSessionLogRepository;

    @Override
    public String logUserLogin(String userId, String email, String ipAddress, String userAgent,
            String deviceType, String browserName, String operatingSystem,
            String location, String loginStatus, String failureReason) {
        String methodName = "logUserLogin";
        logger.info("Entering into " + methodName);

        String sessionId = null;

        try {
            sessionId = UUID.randomUUID().toString();

            UserSessionLog sessionLog = new UserSessionLog();
            sessionLog.setUserId(userId);
            sessionLog.setEmail(email);
            sessionLog.setSessionId(sessionId);
            sessionLog.setAction("LOGIN");
            sessionLog.setLoginTime(new Date());
            sessionLog.setIpAddress(ipAddress);
            sessionLog.setUserAgent(userAgent);
            sessionLog.setDeviceType(deviceType);
            sessionLog.setBrowserName(browserName);
            sessionLog.setOperatingSystem(operatingSystem);
            sessionLog.setLocation(location);
            sessionLog.setLoginStatus(loginStatus);
            sessionLog.setFailureReason(failureReason);
            sessionLog.setCreatedDate(new Date());

            userSessionLogRepository.createSessionLog(sessionLog);

            logger.info("User login logged successfully for user: " + userId + " with status: " + loginStatus);
        } catch (Exception e) {
            logger.severe("Failed to log user login: " + e.getMessage());
            // Don't throw exception to avoid disrupting login flow
        } finally {
            logger.info("Exiting from " + methodName);
        }

        return sessionId;
    }

    @Override
    public void logUserLogout(String sessionId) {
        String methodName = "logUserLogout";
        logger.info("Entering into " + methodName);

        try {
            userSessionLogRepository.updateLogoutTime(sessionId);
            logger.info("User logout logged successfully for session: " + sessionId);
        } catch (Exception e) {
            logger.severe("Failed to log user logout: " + e.getMessage());
            // Don't throw exception to avoid disrupting logout flow
        } finally {
            logger.info("Exiting from " + methodName);
        }
    }

    @Override
    public List<UserSessionLog> getUserSessionLogs(String userId) {
        String methodName = "getUserSessionLogs";
        logger.info("Entering into " + methodName);

        try {
            return userSessionLogRepository.getUserSessionLogs(userId);
        } catch (Exception e) {
            logger.severe("Failed to get user session logs: " + e.getMessage());
            throw new RuntimeException("Failed to get user session logs: " + e.getMessage());
        } finally {
            logger.info("Exiting from " + methodName);
        }
    }

    @Override
    public List<UserSessionLog> getSessionLogsByDateRange(String startDate, String endDate) {
        String methodName = "getSessionLogsByDateRange";
        logger.info("Entering into " + methodName);

        try {
            return userSessionLogRepository.getSessionLogsByDateRange(startDate, endDate);
        } catch (Exception e) {
            logger.severe("Failed to get session logs by date range: " + e.getMessage());
            throw new RuntimeException("Failed to get session logs by date range: " + e.getMessage());
        } finally {
            logger.info("Exiting from " + methodName);
        }
    }

    @Override
    public UserSessionLog getActiveSession(String userId) {
        String methodName = "getActiveSession";
        logger.info("Entering into " + methodName);

        try {
            return userSessionLogRepository.getActiveSession(userId);
        } catch (Exception e) {
            logger.severe("Failed to get active session: " + e.getMessage());
            throw new RuntimeException("Failed to get active session: " + e.getMessage());
        } finally {
            logger.info("Exiting from " + methodName);
        }
    }

    @Override
    public List<UserSessionLog> getFailedLoginAttempts(String email) {
        String methodName = "getFailedLoginAttempts";
        logger.info("Entering into " + methodName);

        try {
            return userSessionLogRepository.getFailedLoginAttempts(email);
        } catch (Exception e) {
            logger.severe("Failed to get failed login attempts: " + e.getMessage());
            throw new RuntimeException("Failed to get failed login attempts: " + e.getMessage());
        } finally {
            logger.info("Exiting from " + methodName);
        }
    }
}
