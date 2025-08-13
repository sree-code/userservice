package com.seemee.userservice.controller;

import com.seemee.userservice.constants.SeeMeeConstants;
import com.seemee.userservice.dto.CartItemRequest;
import com.seemee.userservice.dto.CartResponse;
import com.seemee.userservice.dto.UpdateCartItemRequest;
import com.seemee.userservice.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    Logger logger = Logger.getLogger(CartController.class.getName());

    @Autowired
    CartService cartService;

    /**
     * Add item to cart
     * 
     * @param cartItemRequest Item details to add to cart
     * @return Success message with cart ID
     */
    @CrossOrigin(origins = SeeMeeConstants.FRONTEND_LOCAL_URL)
    @PostMapping("/add")
    public ResponseEntity<String> addItemToCart(@RequestBody CartItemRequest cartItemRequest) {
        logger.info("Adding item to cart for user: " + cartItemRequest.getUserId());

        try {
            String result = cartService.addItemToCart(cartItemRequest);
            return ResponseEntity.ok("Item added to cart successfully. Cart ID: " + result);
        } catch (Exception e) {
            logger.severe("Failed to add item to cart: " + e.getMessage());
            return ResponseEntity.badRequest().body("Failed to add item to cart: " + e.getMessage());
        }
    }

    /**
     * Update cart item quantity and price
     * 
     * @param updateRequest Update details for cart item
     * @return Success/failure message
     */
    @CrossOrigin(origins = SeeMeeConstants.FRONTEND_LOCAL_URL)
    @PutMapping("/update")
    public ResponseEntity<String> updateCartItem(@RequestBody UpdateCartItemRequest updateRequest) {
        logger.info("Updating cart item: " + updateRequest.getCartId() + " for user: " + updateRequest.getUserId());

        try {
            String result = cartService.updateCartItem(updateRequest);

            if (result.contains("successfully")) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.badRequest().body(result);
            }
        } catch (Exception e) {
            logger.severe("Failed to update cart item: " + e.getMessage());
            return ResponseEntity.badRequest().body("Failed to update cart item: " + e.getMessage());
        }
    }

    /**
     * Remove item from cart
     * 
     * @param userId User ID
     * @param cartId Cart item ID to remove
     * @return Success/failure message
     */
    @CrossOrigin(origins = SeeMeeConstants.FRONTEND_LOCAL_URL)
    @DeleteMapping("/remove/{userId}/{cartId}")
    public ResponseEntity<String> removeItemFromCart(@PathVariable String userId, @PathVariable String cartId) {
        logger.info("Removing item from cart: " + cartId + " for user: " + userId);

        try {
            String result = cartService.removeItemFromCart(userId, cartId);

            if (result.contains("successfully")) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.badRequest().body(result);
            }
        } catch (Exception e) {
            logger.severe("Failed to remove item from cart: " + e.getMessage());
            return ResponseEntity.badRequest().body("Failed to remove item from cart: " + e.getMessage());
        }
    }

    /**
     * Get user's cart with all items and totals
     * 
     * @param userId User ID
     * @return Cart details with items and totals
     */
    @CrossOrigin(origins = SeeMeeConstants.FRONTEND_LOCAL_URL)
    @GetMapping("/user/{userId}")
    public ResponseEntity<CartResponse> getUserCart(@PathVariable String userId) {
        logger.info("Getting cart for user: " + userId);

        try {
            CartResponse cartResponse = cartService.getUserCart(userId);
            return ResponseEntity.ok(cartResponse);
        } catch (Exception e) {
            logger.severe("Failed to get user cart: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Clear all items from user's cart
     * 
     * @param userId User ID
     * @return Success/failure message
     */
    @CrossOrigin(origins = SeeMeeConstants.FRONTEND_LOCAL_URL)
    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<String> clearCart(@PathVariable String userId) {
        logger.info("Clearing cart for user: " + userId);

        try {
            String result = cartService.clearCart(userId);

            if (result.contains("successfully")) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.badRequest().body(result);
            }
        } catch (Exception e) {
            logger.severe("Failed to clear cart: " + e.getMessage());
            return ResponseEntity.badRequest().body("Failed to clear cart: " + e.getMessage());
        }
    }

    /**
     * Move cart items to order (checkout process)
     * 
     * @param userId User ID
     * @return Success/failure message
     */
    @CrossOrigin(origins = SeeMeeConstants.FRONTEND_LOCAL_URL)
    @PostMapping("/checkout/{userId}")
    public ResponseEntity<String> moveCartToOrder(@PathVariable String userId) {
        logger.info("Moving cart to order for user: " + userId);

        try {
            String result = cartService.moveCartToOrder(userId);

            if (result.contains("successfully")) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.badRequest().body(result);
            }
        } catch (Exception e) {
            logger.severe("Failed to move cart to order: " + e.getMessage());
            return ResponseEntity.badRequest().body("Failed to move cart to order: " + e.getMessage());
        }
    }

    /**
     * Get cart item count for user
     * 
     * @param userId User ID
     * @return Cart item count
     */
    @CrossOrigin(origins = SeeMeeConstants.FRONTEND_LOCAL_URL)
    @GetMapping("/count/{userId}")
    public ResponseEntity<Integer> getCartItemCount(@PathVariable String userId) {
        logger.info("Getting cart item count for user: " + userId);

        try {
            CartResponse cartResponse = cartService.getUserCart(userId);
            return ResponseEntity.ok(cartResponse.getTotalItems());
        } catch (Exception e) {
            logger.severe("Failed to get cart item count: " + e.getMessage());
            return ResponseEntity.badRequest().body(0);
        }
    }

    /**
     * Get cart total price for user
     * 
     * @param userId User ID
     * @return Cart total price
     */
    @CrossOrigin(origins = SeeMeeConstants.FRONTEND_LOCAL_URL)
    @GetMapping("/total/{userId}")
    public ResponseEntity<String> getCartTotal(@PathVariable String userId) {
        logger.info("Getting cart total for user: " + userId);

        try {
            CartResponse cartResponse = cartService.getUserCart(userId);
            return ResponseEntity.ok(cartResponse.getTotalPrice());
        } catch (Exception e) {
            logger.severe("Failed to get cart total: " + e.getMessage());
            return ResponseEntity.badRequest().body("0.00");
        }
    }
}
