package com.seemee.userservice.repository;

import com.seemee.userservice.model.UserSessionLog;
import java.util.List;

public interface UserSessionLogRepository {
    void createSessionLog(UserSessionLog sessionLog);

    List<UserSessionLog> getUserSessionLogs(String userId);

    List<UserSessionLog> getSessionLogsByDateRange(String startDate, String endDate);

    UserSessionLog getActiveSession(String userId);

    void updateLogoutTime(String sessionId);

    List<UserSessionLog> getFailedLoginAttempts(String email);
}
