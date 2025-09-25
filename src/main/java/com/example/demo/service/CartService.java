package com.example.demo.service;

import com.example.demo.dto.CartItemDTO;
import java.util.List;

public interface CartService {
    List<CartItemDTO> getCartByUser(Long userId);
    CartItemDTO addToCart(Long userId, Long dishId, int quantity);
    CartItemDTO updateQuantity(Long userId, Long dishId, int quantity);
    void removeFromCart(Long userId, Long dishId);
    void clearCart(Long userId);
}
