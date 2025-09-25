package com.example.demo.service;

import com.example.demo.dto.CartItemDTO;
import java.util.List;
import java.util.UUID;

public interface CartService {
    List<CartItemDTO> getCartByUser(UUID userId);
    CartItemDTO addToCart(UUID userId, Long dishId, int quantity);
    CartItemDTO updateQuantity(UUID userId, Long dishId, int quantity);
    void removeFromCart(UUID userId, Long dishId);
    void clearCart(UUID userId);
}
