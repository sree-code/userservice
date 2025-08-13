package com.seemee.userservice.service;

import com.seemee.userservice.dto.CartItemRequest;
import com.seemee.userservice.dto.CartResponse;
import com.seemee.userservice.dto.UpdateCartItemRequest;

public interface CartService {
    String addItemToCart(CartItemRequest cartItemRequest);

    String updateCartItem(UpdateCartItemRequest updateRequest);

    String removeItemFromCart(String userId, String cartId);

    CartResponse getUserCart(String userId);

    String clearCart(String userId);

    String moveCartToOrder(String userId);
}
