package com.seemee.userservice.service;

import com.seemee.userservice.model.UserSessionLog;
import java.util.List;

public interface UserSessionLogService {
    String logUserLogin(String userId, String email, String ipAddress, String userAgent,
            String deviceType, String browserName, String operatingSystem,
            String location, String loginStatus, String failureReason);

    void logUserLogout(String sessionId);

    List<UserSessionLog> getUserSessionLogs(String userId);

    List<UserSessionLog> getSessionLogsByDateRange(String startDate, String endDate);

    UserSessionLog getActiveSession(String userId);

    List<UserSessionLog> getFailedLoginAttempts(String email);
}
