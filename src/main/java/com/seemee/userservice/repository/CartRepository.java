package com.seemee.userservice.repository;

import com.seemee.userservice.dto.CartItemRequest;
import com.seemee.userservice.dto.CartResponse;
import com.seemee.userservice.dto.UpdateCartItemRequest;
import com.seemee.userservice.model.User;

public interface CartRepository {
    String addItemToCart(CartItemRequest cartItemRequest);

    String updateCartItem(UpdateCartItemRequest updateRequest);

    String removeItemFromCart(String userId, String cartId);

    CartResponse getUserCart(String userId);

    String clearCart(String userId);

    User getUserById(String userId);
}
