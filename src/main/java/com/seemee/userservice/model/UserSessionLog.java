package com.seemee.userservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "userSessionLogs")
public class UserSessionLog {

    @Id
    @JsonProperty("_id")
    private String id;

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("email")
    private String email;

    @JsonProperty("sessionId")
    private String sessionId;

    @JsonProperty("action")
    private String action; // LOGIN, LOGOUT, SESSION_TIMEOUT, FORCED_LOGOUT

    @JsonProperty("loginTime")
    private Date loginTime;

    @JsonProperty("logoutTime")
    private Date logoutTime;

    @JsonProperty("sessionDuration")
    private Long sessionDuration; // in milliseconds

    @JsonProperty("ipAddress")
    private String ipAddress;

    @JsonProperty("userAgent")
    private String userAgent;

    @JsonProperty("deviceType")
    private String deviceType; // WEB, MOBILE, TABLET

    @JsonProperty("browserName")
    private String browserName;

    @JsonProperty("operatingSystem")
    private String operatingSystem;

    @JsonProperty("location")
    private String location; // City, Country from IP

    @JsonProperty("loginStatus")
    private String loginStatus; // SUCCESS, FAILED, BLOCKED

    @JsonProperty("failureReason")
    private String failureReason; // Invalid credentials, Account blocked, etc.

    @JsonProperty("createdDate")
    private Date createdDate;
}
