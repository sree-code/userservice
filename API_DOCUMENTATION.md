# User Service API Documentation

This document contains sample requests and responses for all APIs in the User Service.

**Base URLs:**
- **Local Development:** `http://localhost:8080` 
- **Network Access (Dev Mode):** `http://[YOUR_IP]:8080` (Replace [YOUR_IP] with your actual IP address)
- **Production:** `https://your-domain.com`

## 🚀 Development Mode Setup

### Quick Start:
1. **Option 1 - Using VS Code Tasks:**
   - Press `Ctrl+Shift+P` → "Tasks: Run Task" → "Run Spring Boot - Dev Mode (External Access)"

2. **Option 2 - Using Batch Script:**
   ```cmd
   start-dev.bat
   ```

3. **Option 3 - Manual Command:**
   ```cmd
   ./mvnw.cmd spring-boot:run -Dspring-boot.run.jvmArguments="-Dserver.address=0.0.0.0 -Dserver.port=8080 -Dspring.profiles.active=dev"
   ```

### Network Access:
- Find your IP address: `ipconfig` (Windows) or `ifconfig` (Mac/Linux)
- Access from any device: `http://[YOUR_IP]:8080`
- Examples: `http://192.168.1.100:8080` or `http://10.0.0.50:8080`

---

## Table of Contents

1. [Health Check APIs](#health-check-apis)
2. [User Authentication APIs](#user-authentication-apis)
3. [User Profile APIs](#user-profile-apis)
4. [User Management APIs](#user-management-apis)

---

## Health Check APIs

### 1. Health Check
**Endpoint:** `GET /health`  
**Description:** Check if the service is running

#### Request:
```http
GET /health
```

#### Response:
```http
HTTP/1.1 200 OK
Content-Type: text/plain

OK
```

### 2. Home/Root
**Endpoint:** `GET /`  
**Description:** Get service status message

#### Request:
```http
GET /
```

#### Response:
```http
HTTP/1.1 200 OK
Content-Type: text/plain

User Service is running on Render!
```

---

## User Authentication APIs

### 3. User Login
**Endpoint:** `POST /api/user/login`  
**Description:** Authenticate user credentials

#### Request:
```http
POST /api/user/login
Content-Type: application/json

{
  "email": "sivakreddy.k7@gmail.com",
  "password": "password123"
}
```

#### Success Response:
```http
HTTP/1.1 200 OK
Content-Type: application/json

{
  "status": "SUCCESS",
  "message": "Authentication successful",
  "authenticated": true,
  "userId": "krish143",
  "role": "USER,VENDOR",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "_id": "676bbfeb792ce3babea90746",
    "userId": "krish143",
    "email": "sivakreddy.k7@gmail.com",
    "firstName": "Siva KrishnaReddy",
    "lastName": "Kasa",
    "phone": "7396391972",
    "address": [
      {
        "index": 1,
        "houseNo": "D-25&26",
        "apartmentName": "NSK's Royale",
        "landmark": "JanapriyaNagar",
        "city": "Hyderabad",
        "state": "Telangana",
        "zipCode": "500049",
        "country": "India",
        "addressType": "HOME"
      }
    ],
    "role": ["USER", "VENDOR"],
    "status": true,
    "createdDate": "2024-12-19T00:00:00.000Z",
    "updatedDate": "2024-12-19T00:00:00.000Z",
    "subscription": ["PREMIUM", "GOLD"]
  }
}
```

#### Failure Response:
```http
HTTP/1.1 200 OK
Content-Type: application/json

{
  "status": "FAILURE",
  "message": "Invalid credentials",
  "authenticated": false,
  "userId": null,
  "role": null,
  "token": null,
  "user": null
}
```

---

## User Profile APIs

### 4. Get User Profile
**Endpoint:** `GET /api/user/getProfile/{email}`  
**Description:** Retrieve user profile by email

#### Request:
```http
GET /api/user/getProfile/sivakreddy.k7@gmail.com
```

#### Success Response:
```http
HTTP/1.1 200 OK
Content-Type: application/json

{
  "_id": "676bbfeb792ce3babea90746",
  "userId": "krish143",
  "email": "sivakreddy.k7@gmail.com",
  "firstName": "Siva KrishnaReddy",
  "lastName": "Kasa",
  "phone": "7396391972",
  "address": [
    {
      "index": 1,
      "houseNo": "D-25&26",
      "apartmentName": "NSK's Royale",
      "landmark": "JanapriyaNagar",
      "city": "Hyderabad",
      "state": "Telangana",
      "zipCode": "500049",
      "country": "India",
      "addressType": "HOME"
    },
    {
      "index": 2,
      "houseNo": "18/524",
      "apartmentName": "Jhansi Nagar",
      "landmark": "Porddatur",
      "city": "Kadapa",
      "state": "AndhraPradesh",
      "zipCode": "516360",
      "country": "India",
      "addressType": "WORK"
    }
  ],
  "role": ["USER", "VENDOR"],
  "status": true,
  "createdDate": "2024-12-19T00:00:00.000Z",
  "updatedDate": "2024-12-19T00:00:00.000Z",
  "lastLoginDate": "2025-07-20T10:30:00.000Z",
  "lastLoginIp": "192.168.1.100",
  "subscription": ["PREMIUM", "GOLD"],
  "bankAccount": [
    {
      "bankName": "HDFC BANK",
      "accountNumber": "7396391972",
      "accountType": "Salaried",
      "routingNumber": "HDFC0001234"
    }
  ],
  "creditCard": [],
  "upi": [
    {
      "upiId": "krish143@paytm",
      "status": "ACTIVE",
      "createdDate": "2024-12-19T00:00:00.000Z",
      "updatedDate": "2024-12-19T00:00:00.000Z"
    }
  ],
  "cart": [],
  "orders": [
    {
      "orderId": "2345",
      "orderDate": "2024-12-19T00:00:00.000Z",
      "orderStatus": "COMPLETED",
      "orderTotalPrice": "120",
      "orderItems": [
        {
          "itemId": "Ironing",
          "itemType": "Iron",
          "itemPrice": "10",
          "itemQuantity": "12",
          "itemTotalPrice": "120"
        }
      ]
    }
  ]
}
```

#### User Not Found Response:
```http
HTTP/1.1 200 OK
Content-Type: application/json

{
  "_id": null,
  "userId": null,
  "email": null,
  "firstName": null,
  "lastName": null,
  "phone": null,
  "address": null,
  "role": null,
  "status": false
}
```

### 5. Create User Profile
**Endpoint:** `POST /api/user/createProfile`  
**Description:** Create a new user profile

#### Request:
```http
POST /api/user/createProfile
Content-Type: application/json

{
  "userId": "raghav2025",
  "email": "raghav.sharma@gmail.com",
  "password": "securePassword123",
  "firstName": "Raghav",
  "lastName": "Sharma",
  "phone": "9876543210",
  "address": [
    {
      "index": 1,
      "houseNo": "Plot 42, Flat 304",
      "apartmentName": "Phoenix Towers",
      "landmark": "Near HITEC City Metro Station",
      "city": "Hyderabad",
      "state": "Telangana",
      "zipCode": "500081",
      "country": "India",
      "addressType": "HOME"
    }
  ],
  "role": ["USER"],
  "status": true,
  "createdDate": "2025-07-20T10:30:00.000Z",
  "updatedDate": "2025-07-20T10:30:00.000Z",
  "subscription": ["BASIC"],
  "bankAccount": [
    {
      "bankName": "State Bank of India",
      "accountNumber": "98765432101",
      "accountType": "Savings",
      "routingNumber": "SBIN0001234"
    }
  ],
  "upi": [
    {
      "upiId": "raghav@paytm",
      "status": "ACTIVE",
      "createdDate": "2025-07-20T10:30:00.000Z",
      "updatedDate": "2025-07-20T10:30:00.000Z"
    }
  ],
  "cart": [],
  "orders": []
}
```

#### Success Response:
```http
HTTP/1.1 200 OK
Content-Type: text/plain

User created successfully
```

#### Error Response:
```http
HTTP/1.1 500 Internal Server Error
Content-Type: application/json

{
  "error": "Failed to create user profile: E11000 duplicate key error"
}
```

---

## User Management APIs

### 6. Update Password
**Endpoint:** `POST /api/user/updatePassword`  
**Description:** Update user password

#### Request:
```http
POST /api/user/updatePassword
Content-Type: application/json

{
  "email": "sivakreddy.k7@gmail.com",
  "password": "newSecurePassword123"
}
```

#### Success Response:
```http
HTTP/1.1 200 OK
Content-Type: text/plain

Password updated successfully
```

#### Failure Response:
```http
HTTP/1.1 200 OK
Content-Type: text/plain

Password update failed, Please try again after sometime
```

### 7. Update Address
**Endpoint:** `POST /api/user/address/{index}`  
**Description:** Update specific address by index

#### Request:
```http
POST /api/user/address/1
Content-Type: application/json

{
  "email": "sivakreddy.k7@gmail.com",
  "address": [
    {
      "index": 1,
      "houseNo": "D-25&26 Updated",
      "apartmentName": "NSK's Royale Premium",
      "landmark": "JanapriyaNagar Metro Station",
      "city": "Hyderabad",
      "state": "Telangana",
      "zipCode": "500049",
      "country": "India",
      "addressType": "HOME"
    }
  ]
}
```

#### Success Response:
```http
HTTP/1.1 200 OK
Content-Type: text/plain

Address updated successfully
```

#### Failure Response:
```http
HTTP/1.1 200 OK
Content-Type: text/plain

Address update failed
```

### 8. Update Profile (Not Implemented)
**Endpoint:** `POST /api/user/updateProfile`  
**Description:** Update user profile (Currently returns success message but not implemented)

#### Request:
```http
POST /api/user/updateProfile
Content-Type: application/json

{
  "email": "sivakreddy.k7@gmail.com",
  "firstName": "Updated FirstName",
  "lastName": "Updated LastName",
  "phone": "9999999999"
}
```

#### Response:
```http
HTTP/1.1 200 OK
Content-Type: text/plain

Profile updated successfully
```

---

## Error Responses

### Common Error Codes:

#### 400 Bad Request
```http
HTTP/1.1 400 Bad Request
Content-Type: application/json

{
  "error": "Invalid request format",
  "message": "Required field missing"
}
```

#### 404 Not Found
```http
HTTP/1.1 404 Not Found
Content-Type: application/json

{
  "error": "User not found",
  "message": "No user found with the provided email"
}
```

#### 500 Internal Server Error
```http
HTTP/1.1 500 Internal Server Error
Content-Type: application/json

{
  "error": "Internal server error",
  "message": "Database connection failed"
}
```

---

## Request Headers

### Common Headers:
```http
Content-Type: application/json
Accept: application/json
Origin: http://localhost:3000  # For CORS
```

---

## Notes:

1. **Password Security:** Passwords are hashed using BCrypt before storage
2. **CORS:** The service supports CORS for frontend applications
3. **MongoDB:** All data is stored in MongoDB with automatic ObjectId generation
4. **Address Updates:** Only non-null fields are updated, existing data is preserved
5. **Authentication:** Login returns JWT token for session management
6. **Date Format:** All dates are in ISO 8601 format (UTC)

---

## Testing with cURL Examples:

### Login:
```bash
curl -X POST http://localhost:8080/api/user/login \
  -H "Content-Type: application/json" \
  -d '{"email":"sivakreddy.k7@gmail.com","password":"password123"}'
```

### Get Profile:
```bash
curl -X GET http://localhost:8080/api/user/getProfile/sivakreddy.k7@gmail.com
```

### Create User:
```bash
curl -X POST http://localhost:8080/api/user/createProfile \
  -H "Content-Type: application/json" \
  -d @new-user.json
```

### Update Address:
```bash
curl -X POST http://localhost:8080/api/user/address/1 \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","address":[{"index":1,"city":"New City"}]}'
```

---

**Last Updated:** July 20, 2025  
**Version:** 1.0  
**Service:** User Service  
**Environment:** Development/Production
