package com.seemee.userservice.service.impl;

import com.seemee.userservice.dto.CartItemRequest;
import com.seemee.userservice.dto.CartResponse;
import com.seemee.userservice.dto.UpdateCartItemRequest;
import com.seemee.userservice.repository.CartRepository;
import com.seemee.userservice.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class CartServiceImpl implements CartService {
    Logger logger = Logger.getLogger(CartServiceImpl.class.getName());

    @Autowired
    CartRepository cartRepository;

    @Override
    public String addItemToCart(CartItemRequest cartItemRequest) {
        String methodName = "addItemToCart";
        logger.info("Entering into " + methodName);
        logger.info("Adding item to cart for user: " + cartItemRequest.getUserId());

        try {
            String result = cartRepository.addItemToCart(cartItemRequest);
            logger.info("Item added to cart successfully for user: " + cartItemRequest.getUserId());
            return result;
        } catch (Exception e) {
            logger.severe("Failed to add item to cart for user: " + cartItemRequest.getUserId() + ". Error: "
                    + e.getMessage());
            throw new RuntimeException("Failed to add item to cart: " + e.getMessage());
        } finally {
            logger.info("Exiting from " + methodName);
        }
    }

    @Override
    public String updateCartItem(UpdateCartItemRequest updateRequest) {
        String methodName = "updateCartItem";
        logger.info("Entering into " + methodName);
        logger.info("Updating cart item: " + updateRequest.getCartId() + " for user: " + updateRequest.getUserId());

        try {
            String result = cartRepository.updateCartItem(updateRequest);
            logger.info("Cart item updated successfully: " + updateRequest.getCartId());
            return result;
        } catch (Exception e) {
            logger.severe("Failed to update cart item: " + updateRequest.getCartId() + ". Error: " + e.getMessage());
            throw new RuntimeException("Failed to update cart item: " + e.getMessage());
        } finally {
            logger.info("Exiting from " + methodName);
        }
    }

    @Override
    public String removeItemFromCart(String userId, String cartId) {
        String methodName = "removeItemFromCart";
        logger.info("Entering into " + methodName);
        logger.info("Removing item from cart: " + cartId + " for user: " + userId);

        try {
            String result = cartRepository.removeItemFromCart(userId, cartId);
            logger.info("Item removed from cart successfully: " + cartId);
            return result;
        } catch (Exception e) {
            logger.severe("Failed to remove item from cart: " + cartId + ". Error: " + e.getMessage());
            throw new RuntimeException("Failed to remove item from cart: " + e.getMessage());
        } finally {
            logger.info("Exiting from " + methodName);
        }
    }

    @Override
    public CartResponse getUserCart(String userId) {
        String methodName = "getUserCart";
        logger.info("Entering into " + methodName);
        logger.info("Getting cart for user: " + userId);

        try {
            CartResponse cartResponse = cartRepository.getUserCart(userId);
            logger.info("Retrieved cart for user: " + userId + " with " + cartResponse.getTotalItems() + " items");
            return cartResponse;
        } catch (Exception e) {
            logger.severe("Failed to get cart for user: " + userId + ". Error: " + e.getMessage());
            throw new RuntimeException("Failed to get user cart: " + e.getMessage());
        } finally {
            logger.info("Exiting from " + methodName);
        }
    }

    @Override
    public String clearCart(String userId) {
        String methodName = "clearCart";
        logger.info("Entering into " + methodName);
        logger.info("Clearing cart for user: " + userId);

        try {
            String result = cartRepository.clearCart(userId);
            logger.info("Cart cleared successfully for user: " + userId);
            return result;
        } catch (Exception e) {
            logger.severe("Failed to clear cart for user: " + userId + ". Error: " + e.getMessage());
            throw new RuntimeException("Failed to clear cart: " + e.getMessage());
        } finally {
            logger.info("Exiting from " + methodName);
        }
    }

    @Override
    public String moveCartToOrder(String userId) {
        String methodName = "moveCartToOrder";
        logger.info("Entering into " + methodName);
        logger.info("Moving cart to order for user: " + userId);

        try {
            // Get current cart
            CartResponse cartResponse = cartRepository.getUserCart(userId);

            if (cartResponse.getTotalItems() == 0) {
                return "Cart is empty. Cannot create order.";
            }

            // Here you would typically create an order from cart items
            // For now, we'll just clear the cart after "moving" to order
            String result = cartRepository.clearCart(userId);

            if (result.contains("successfully")) {
                logger.info("Cart moved to order successfully for user: " + userId);
                return "Cart items moved to order successfully";
            } else {
                return "Failed to move cart to order";
            }
        } catch (Exception e) {
            logger.severe("Failed to move cart to order for user: " + userId + ". Error: " + e.getMessage());
            throw new RuntimeException("Failed to move cart to order: " + e.getMessage());
        } finally {
            logger.info("Exiting from " + methodName);
        }
    }
}
