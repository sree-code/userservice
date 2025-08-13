# UserService API Documentation

## Base URL
```
http://localhost:8080
```
or your deployed backend URL

---

## 🔐 USER MANAGEMENT ENDPOINTS

### 1. **getUserProfile**
**Endpoint:** `GET /api/user/getProfile/{email}`

**Description:** Retrieve user profile information by email address

**Parameters:**
- `email` (Path Parameter): User's email address

**Example Request:**
```http
GET /api/user/getProfile/john.doe@example.com
```

**Example Response:**
```json
{
  "_id": "user-id-123",
  "userId": "user123",
  "email": "john.doe@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "phone": "+1234567890",
  "address": [
    {
      "index": 0,
      "houseNo": "123",
      "apartmentName": "Sunshine Apartments",
      "landmark": "Near City Mall",
      "street": "Main Street",
      "city": "New York",
      "state": "NY",
      "zipCode": "10001",
      "country": "USA",
      "addressType": "HOME"
    }
  ],
  "role": ["USER"],
  "status": true,
  "createdDate": "2025-08-01T10:00:00.000Z",
  "updatedDate": "2025-08-01T10:00:00.000Z"
}
```

---

### 2. **authenticateUser** (Basic Login)
**Endpoint:** `POST /api/user/login`

**Description:** Basic user authentication without session logging

**Request Body:**
```json
{
  "email": "john.doe@example.com",
  "password": "password123"
}
```

**Example Response:**
```json
{
  "status": "SUCCESS",
  "message": null,
  "authenticated": true,
  "userId": "user123",
  "role": "USER",
  "user": {
    "userId": "user123",
    "email": "john.doe@example.com",
    "firstName": "John",
    "lastName": "Doe"
  },
  "token": "user123john.doe@example.com6"
}
```

---

### 3. **authenticateUserWithLogging** ⭐ (Enhanced Login)
**Endpoint:** `POST /api/user/loginWithLogging`

**Description:** Enhanced user authentication with comprehensive session logging

**Request Body:**
```json
{
  "email": "john.doe@example.com",
  "password": "password123",
  "ipAddress": "192.168.1.100",
  "userAgent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36",
  "deviceType": "WEB",
  "browserName": "Chrome",
  "operatingSystem": "Windows 10",
  "location": "New York, USA"
}
```

**Example Response:**
```json
{
  "status": "SUCCESS",
  "message": null,
  "authenticated": true,
  "userId": "user123",
  "role": "USER",
  "user": {
    "userId": "user123",
    "email": "john.doe@example.com",
    "firstName": "John",
    "lastName": "Doe"
  },
  "token": "user123john.doe@example.com6",
  "sessionId": "session-uuid-abc-123"
}
```

**Login Failure Response:**
```json
{
  "status": "FAILED",
  "message": null,
  "authenticated": false,
  "userId": null,
  "role": null,
  "user": null,
  "token": null,
  "sessionId": null
}
```

---

### 4. **logoutUser**
**Endpoint:** `POST /api/user/logout`

**Description:** Log out user and end session with logging

**Request Body:**
```json
{
  "sessionId": "session-uuid-abc-123",
  "userId": "user123"
}
```

**Example Response:**
```json
"User logged out successfully"
```

---

### 5. **createUserProfile**
**Endpoint:** `POST /api/user/createProfile`

**Description:** Create a new user profile and send welcome email

**Request Body:**
```json
{
  "userId": "user123",
  "email": "john.doe@example.com",
  "password": "password123",
  "firstName": "John",
  "lastName": "Doe",
  "phone": "+1234567890",
  "role": ["USER"],
  "status": true,
  "address": [
    {
      "index": 0,
      "houseNo": "123",
      "apartmentName": "Sunshine Apartments",
      "landmark": "Near City Mall",
      "street": "Main Street",
      "city": "New York",
      "state": "NY",
      "zipCode": "10001",
      "country": "USA",
      "addressType": "HOME"
    }
  ],
  "bankAccount": [
    {
      "bankName": "Chase Bank",
      "accountNumber": "1234567890",
      "accountType": "CHECKING",
      "routingNumber": "021000021"
    }
  ],
  "creditCard": [
    {
      "cardNumber": "4111111111111111",
      "cardType": "VISA",
      "expirationDate": "2027-12-31T00:00:00.000Z",
      "status": "ACTIVE"
    }
  ],
  "upi": [
    {
      "upiId": "john.doe@paytm",
      "status": "ACTIVE"
    }
  ]
}
```

**Example Response:**
```json
"User created successfully. A welcome email has been sent to john.doe@example.com"
```

---

### 6. **updatePassword**
**Endpoint:** `POST /api/user/updatePassword`

**Description:** Update user password

**Request Body:**
```json
{
  "email": "john.doe@example.com",
  "password": "newPassword123"
}
```

**Example Response (Success):**
```json
"Password updated successfully"
```

**Example Response (Failure):**
```json
"Password update failed, Please try again after sometime"
```

---

### 7. **updateAddress**
**Endpoint:** `POST /api/user/address/{index}`

**Description:** Update user address at specific index

**Parameters:**
- `index` (Path Parameter): Address index to update (e.g., "0", "1")

**Example Request:**
```http
POST /api/user/address/0
```

**Request Body:**
```json
{
  "userId": "user123",
  "address": [
    {
      "index": 0,
      "houseNo": "456",
      "apartmentName": "New Apartments",
      "landmark": "Near Central Park",
      "street": "Updated Street",
      "city": "New York",
      "state": "NY",
      "zipCode": "10002",
      "country": "USA",
      "addressType": "HOME"
    }
  ]
}
```

**Example Response:**
```json
"Address updated successfully"
```

---

### 8. **updateProfile**
**Endpoint:** `POST /api/user/updateProfile`

**Description:** Update user profile information (Currently placeholder)

**Request Body:**
```json
{
  "userId": "user123",
  "firstName": "John",
  "lastName": "Doe",
  "phone": "+1234567890"
}
```

**Example Response:**
```json
"Profile updated successfully"
```

---

## 📋 **REQUEST/RESPONSE STRUCTURES**

### **AuthenticateUser DTO:**
```json
{
  "email": "string",
  "password": "string"
}
```

### **LoginRequest DTO:**
```json
{
  "email": "string",
  "password": "string",
  "ipAddress": "string",
  "userAgent": "string",
  "deviceType": "string", // WEB, MOBILE, TABLET
  "browserName": "string",
  "operatingSystem": "string",
  "location": "string"
}
```

### **LogoutRequest DTO:**
```json
{
  "sessionId": "string",
  "userId": "string"
}
```

### **LoginResponse DTO:**
```json
{
  "status": "string", // SUCCESS, FAILED
  "message": "string",
  "authenticated": "boolean",
  "userId": "string",
  "role": "string",
  "user": "User object",
  "token": "string",
  "sessionId": "string" // Only in enhanced login
}
```

### **User Model (Complete Structure):**
```json
{
  "_id": "string",
  "userId": "string",
  "email": "string",
  "password": "string", // Encrypted
  "firstName": "string",
  "lastName": "string",
  "phone": "string",
  "address": [
    {
      "index": "number",
      "houseNo": "string",
      "apartmentName": "string",
      "landmark": "string",
      "street": "string",
      "city": "string",
      "state": "string",
      "zipCode": "string",
      "country": "string",
      "addressType": "string" // HOME, WORK, OTHER
    }
  ],
  "role": ["string"], // Array of roles
  "status": "boolean",
  "createdDate": "Date",
  "updatedDate": "Date",
  "lastLoginDate": "Date",
  "lastLoginIp": "string",
  "loginBlockedDate": "Date",
  "loginBlockedIp": "string",
  "loginBlockedReason": "string",
  "subscription": ["string"],
  "bankAccount": [
    {
      "bankName": "string",
      "accountNumber": "string",
      "accountType": "string",
      "routingNumber": "string"
    }
  ],
  "creditCard": [
    {
      "cardNumber": "string",
      "cardType": "string",
      "expirationDate": "Date",
      "status": "string",
      "createdDate": "Date",
      "updatedDate": "Date"
    }
  ],
  "upi": [
    {
      "upiId": "string",
      "status": "string",
      "createdDate": "Date",
      "updatedDate": "Date"
    }
  ],
  "cart": [
    {
      "cartId": "string",
      "itemId": "string",
      "itemType": "string",
      "itemDescription": "string",
      "itemPrice": "string",
      "itemQuantity": "string",
      "itemTotalPrice": "string"
    }
  ],
  "orders": [
    {
      "orderId": "string",
      "orderDate": "Date",
      "orderStatus": "string",
      "orderTotalPrice": "string",
      "orderItems": ["OrderItem objects"]
    }
  ]
}
```

---

## 🔧 **Error Handling**

### **Common HTTP Status Codes:**
- `200 OK` - Request successful
- `400 Bad Request` - Invalid request data
- `401 Unauthorized` - Authentication failed
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error

### **Error Response Format:**
```json
{
  "error": "Error message describing what went wrong"
}
```

---

## 🎯 **Usage Notes**

1. **Enhanced Login vs Basic Login:**
   - Use `authenticateUserWithLogging` for comprehensive session tracking
   - Use `authenticateUser` for basic authentication without detailed logging

2. **Session Management:**
   - Store `sessionId` from enhanced login response
   - Use `sessionId` for logout functionality
   - Session tracking provides device and location information

3. **Address Management:**
   - Addresses are stored as an array
   - Use index to update specific addresses
   - Multiple addresses per user supported

4. **Security:**
   - Passwords are automatically encrypted using BCrypt
   - All endpoints support CORS for frontend integration
   - Token-based authentication implemented

5. **Email Integration:**
   - Welcome emails sent automatically on user creation
   - Email service handles delivery and logging

This documentation provides complete coverage of all UserService endpoints for UI integration.

---

## 🛒 **CART MANAGEMENT ENDPOINTS**

### 1. **addItemToCart**
**Endpoint:** `POST /api/cart/add`

**Description:** Add item to user's cart with automatic price calculation

**Request Body:**
```json
{
  "userId": "user123",
  "itemId": "item001",
  "itemType": "PRODUCT",
  "itemDescription": "Wireless Headphones",
  "itemPrice": "99.99",
  "itemQuantity": "2"
}
```

**Example Response:**
```json
"Item added to cart successfully. Cart ID: cart-uuid-123"
```

**Note:** If item already exists in cart, quantity will be updated automatically.

---

### 2. **updateCartItem**
**Endpoint:** `PUT /api/cart/update`

**Description:** Update cart item quantity and price with automatic total calculation

**Request Body:**
```json
{
  "userId": "user123",
  "cartId": "cart-uuid-123",
  "itemQuantity": "3",
  "itemPrice": "89.99"
}
```

**Example Response:**
```json
"Cart item updated successfully"
```

---

### 3. **removeItemFromCart**
**Endpoint:** `DELETE /api/cart/remove/{userId}/{cartId}`

**Description:** Remove specific item from cart

**Parameters:**
- `userId` (Path Parameter): User ID
- `cartId` (Path Parameter): Cart item ID to remove

**Example Request:**
```http
DELETE /api/cart/remove/user123/cart-uuid-123
```

**Example Response:**
```json
"Item removed from cart successfully"
```

---

### 4. **getUserCart**
**Endpoint:** `GET /api/cart/user/{userId}`

**Description:** Get complete cart details with all items and totals

**Parameters:**
- `userId` (Path Parameter): User ID

**Example Request:**
```http
GET /api/cart/user/user123
```

**Example Response:**
```json
{
  "userId": "user123",
  "cartItems": [
    {
      "cartId": "cart-uuid-123",
      "itemId": "item001",
      "itemType": "PRODUCT",
      "itemDescription": "Wireless Headphones",
      "itemPrice": "99.99",
      "itemQuantity": "2",
      "itemTotalPrice": "199.98",
      "addedDate": "2025-08-01T10:00:00.000Z"
    },
    {
      "cartId": "cart-uuid-456",
      "itemId": "item002",
      "itemType": "SERVICE",
      "itemDescription": "Installation Service",
      "itemPrice": "25.00",
      "itemQuantity": "1",
      "itemTotalPrice": "25.00",
      "addedDate": "2025-08-01T10:30:00.000Z"
    }
  ],
  "totalItems": 2,
  "totalPrice": "224.98",
  "lastUpdated": "2025-08-01T11:00:00.000Z"
}
```

---

### 5. **clearCart**
**Endpoint:** `DELETE /api/cart/clear/{userId}`

**Description:** Remove all items from user's cart

**Parameters:**
- `userId` (Path Parameter): User ID

**Example Request:**
```http
DELETE /api/cart/clear/user123
```

**Example Response:**
```json
"Cart cleared successfully"
```

---

### 6. **moveCartToOrder** (Checkout)
**Endpoint:** `POST /api/cart/checkout/{userId}`

**Description:** Move cart items to order process (checkout)

**Parameters:**
- `userId` (Path Parameter): User ID

**Example Request:**
```http
POST /api/cart/checkout/user123
```

**Example Response:**
```json
"Cart items moved to order successfully"
```

---

### 7. **getCartItemCount**
**Endpoint:** `GET /api/cart/count/{userId}`

**Description:** Get total number of items in cart

**Parameters:**
- `userId` (Path Parameter): User ID

**Example Request:**
```http
GET /api/cart/count/user123
```

**Example Response:**
```json
3
```

---

### 8. **getCartTotal**
**Endpoint:** `GET /api/cart/total/{userId}`

**Description:** Get total price of all items in cart

**Parameters:**
- `userId` (Path Parameter): User ID

**Example Request:**
```http
GET /api/cart/total/user123
```

**Example Response:**
```json
"224.98"
```

---

## 📋 **CART DATA STRUCTURES**

### **CartItemRequest DTO:**
```json
{
  "userId": "string",
  "itemId": "string",
  "itemType": "string", // PRODUCT, SERVICE
  "itemDescription": "string",
  "itemPrice": "string", // Decimal as string
  "itemQuantity": "string" // Integer as string
}
```

### **UpdateCartItemRequest DTO:**
```json
{
  "userId": "string",
  "cartId": "string",
  "itemQuantity": "string",
  "itemPrice": "string"
}
```

### **CartResponse DTO:**
```json
{
  "userId": "string",
  "cartItems": [
    {
      "cartId": "string",
      "itemId": "string",
      "itemType": "string",
      "itemDescription": "string",
      "itemPrice": "string",
      "itemQuantity": "string",
      "itemTotalPrice": "string", // Auto-calculated
      "addedDate": "Date",
      "updatedDate": "Date"
    }
  ],
  "totalItems": "number",
  "totalPrice": "string", // Auto-calculated sum
  "lastUpdated": "string"
}
```

---

## 🎯 **CART USAGE NOTES**

1. **Automatic Price Calculation:**
   - Item total price is automatically calculated (price × quantity)
   - Cart total price is automatically calculated (sum of all item totals)

2. **Duplicate Item Handling:**
   - Adding an existing item updates the quantity instead of creating duplicate
   - Quantities are additive when adding existing items

3. **Cart Persistence:**
   - Cart items are stored in user document
   - Cart persists across sessions
   - Each cart item has unique cartId for operations

4. **Validation:**
   - Price and quantity are stored as strings but validated as numbers
   - Empty cart operations return appropriate messages

5. **Integration with Orders:**
   - Use `moveCartToOrder` for checkout process
   - Cart is cleared after successful order creation
