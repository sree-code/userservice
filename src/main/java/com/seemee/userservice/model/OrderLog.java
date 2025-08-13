package com.seemee.userservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "orderLogs")
public class OrderLog {

    @Id
    @JsonProperty("_id")
    private String id;

    @JsonProperty("orderId")
    private String orderId;

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("action")
    private String action; // CREATE, UPDATE, CANCEL, ADDRESS_UPDATE, STATUS_CHANGE

    @JsonProperty("previousStatus")
    private String previousStatus;

    @JsonProperty("newStatus")
    private String newStatus;

    @JsonProperty("description")
    private String description;

    @JsonProperty("actionBy")
    private String actionBy; // USER, ADMIN, SYSTEM

    @JsonProperty("timestamp")
    private Date timestamp;

    @JsonProperty("ipAddress")
    private String ipAddress;

    @JsonProperty("userAgent")
    private String userAgent;

    @JsonProperty("additionalDetails")
    private String additionalDetails; // JSON string for any additional data

    @JsonProperty("createdDate")
    private Date createdDate;
}
