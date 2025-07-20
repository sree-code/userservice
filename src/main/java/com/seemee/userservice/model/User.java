package com.seemee.userservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.seemee.userservice.dto.Address;
import com.seemee.userservice.util.ObjectIdDeserializer;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Document(collection = "users")
public class User {

    @Id
    @JsonProperty("_id")
    @JsonDeserialize(using = ObjectIdDeserializer.class)
    private String id;
    @JsonProperty("userId")
    private String userId;
    @JsonProperty("email")
    private String email;
    @JsonProperty("password")
    private String password;
    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("lastName")
    private String lastName;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("address")
    private List<Address> address;
    @JsonProperty("role")
    private List<String> role;
    @JsonProperty("status")
    private boolean status;
    @JsonProperty("createdDate")
    private Date createdDate;
    @JsonProperty("updatedDate")
    private Date updatedDate;
    @JsonProperty("lastLoginDate")
    private Date lastLoginDate;
    @JsonProperty("lastLoginIp")
    private String lastLoginIp;
    @JsonProperty("loginBlockedDate")
    private Date loginBlockedDate;
    @JsonProperty("loginBlockedIp")
    private String loginBlockedIp;
    @JsonProperty("loginBlockedReason")
    private String loginBlockedReason;
    @JsonProperty("subscription")
    private List<String> subscription;
    @JsonProperty("bankAccount")
    private List<BankAccount> bankAccount;
    @JsonProperty("creditCard")
    private List<CreditCard> creditCard;
    @JsonProperty("upi")
    private List<Upi> upi;
    @JsonProperty("cart")
    private List<CartItem> cart;
    @JsonProperty("orders")
    private List<Order> orders;

    // Getters and Setters
    // ...
}

@Data
class BankAccount {
    private String bankName;
    private String accountNumber;
    private String accountType;
    private String routingNumber;

    // Getters and Setters
    // ...
}

@Data
class CreditCard {
    private String cardNumber;
    private String cardType;
    private Date expirationDate;
    private String status;
    private Date createdDate;
    private Date updatedDate;

    // Getters and Setters
    // ...
}

@Data
class Upi {
    private String upiId;
    private String status;
    private Date createdDate;
    private Date updatedDate;

    // Getters and Setters
    // ...
}

@Data
class CartItem {
    @JsonProperty("cartId")
    private String cartId;
    @JsonProperty("itemId")
    private String itemId;
    @JsonProperty("itemType")
    private String itemType;
    @JsonProperty("itemDescription")
    private String itemDescription;
    @JsonProperty("itemPrice")
    private String itemPrice;
    @JsonProperty("itemQuantity")
    private String itemQuantity;
    @JsonProperty("itemTotalPrice")
    private String itemTotalPrice;

    // Getters and Setters
    // ...
}

@Data
class Order {
    @JsonProperty("orderId")
    private String orderId;
    @JsonProperty("orderDate")
    private Date orderDate;
    @JsonProperty("orderStatus")
    private String orderStatus;
    @JsonProperty("orderTotalPrice")
    private String orderTotalPrice;
    @JsonProperty("orderItems")
    private List<OrderItem> orderItems;

    // Getters and Setters
    // ...
}

@Data
class OrderItem {
    @JsonProperty("itemId")
    private String itemId;
    @JsonProperty("itemType")
    private String itemType;
    @JsonProperty("itemPrice")
    private String itemPrice;
    @JsonProperty("itemQuantity")
    private String itemQuantity;
    @JsonProperty("itemTotalPrice")
    private String itemTotalPrice;

    // Getters and Setters
    // ...
}