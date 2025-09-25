package com.example.demo.service.impl;

import com.example.demo.dto.CartItemDTO;
import com.example.demo.entity.CartItem;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public List<CartItemDTO> getCartByUser(Long userId) {
        return cartItemRepository.findByUserId(userId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CartItemDTO addToCart(Long userId, Long dishId, int quantity) {
        CartItem item = cartItemRepository.findByUserIdAndDishId(userId, dishId);
        if (item != null) {
            // nếu món đã có trong giỏ, cộng thêm số lượng
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            item = CartItem.builder()
                    .userId(userId)
                    .dishId(dishId)
                    .quantity(quantity)
                    .build();
        }
        return toDTO(cartItemRepository.save(item));
    }

    @Override
    public CartItemDTO updateQuantity(Long userId, Long dishId, int quantity) {
        CartItem item = cartItemRepository.findByUserIdAndDishId(userId, dishId);
        if (item != null) {
            item.setQuantity(quantity);
            return toDTO(cartItemRepository.save(item));
        }
        return null;
    }

    @Override
    public void removeFromCart(Long userId, Long dishId) {
        CartItem item = cartItemRepository.findByUserIdAndDishId(userId, dishId);
        if (item != null) {
            cartItemRepository.delete(item);
        }
    }

    @Override
    public void clearCart(Long userId) {
        List<CartItem> items = cartItemRepository.findByUserId(userId);
        cartItemRepository.deleteAll(items);
    }

    private CartItemDTO toDTO(CartItem item) {
        return new CartItemDTO(item.getId(), item.getUserId(), item.getDishId(), item.getQuantity());
    }
}
