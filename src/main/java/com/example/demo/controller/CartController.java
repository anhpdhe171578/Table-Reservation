package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.CartItemDTO;
import com.example.demo.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<CartItemDTO>>> getCart(@PathVariable Long userId) {
        List<CartItemDTO> cart = cartService.getCartByUser(userId);
        return ResponseEntity.ok(new ApiResponse<>("success", "Lấy giỏ hàng thành công", cart));
    }

    @PostMapping("/{userId}/add")
    public ResponseEntity<ApiResponse<CartItemDTO>> addToCart(
            @PathVariable Long userId,
            @RequestParam Long dishId,
            @RequestParam int quantity) {
        CartItemDTO item = cartService.addToCart(userId, dishId, quantity);
        return ResponseEntity.ok(new ApiResponse<>("success", "Thêm món vào giỏ hàng thành công", item));
    }

    @PutMapping("/{userId}/update")
    public ResponseEntity<ApiResponse<CartItemDTO>> updateCart(
            @PathVariable Long userId,
            @RequestParam Long dishId,
            @RequestParam int quantity) {
        CartItemDTO item = cartService.updateQuantity(userId, dishId, quantity);
        if (item == null)
            return ResponseEntity.ok(new ApiResponse<>("error", "Món không tồn tại trong giỏ hàng", null));
        return ResponseEntity.ok(new ApiResponse<>("success", "Cập nhật số lượng thành công", item));
    }

    @DeleteMapping("/{userId}/remove")
    public ResponseEntity<ApiResponse<Void>> removeFromCart(
            @PathVariable Long userId,
            @RequestParam Long dishId) {
        cartService.removeFromCart(userId, dishId);
        return ResponseEntity.ok(new ApiResponse<>("success", "Xóa món khỏi giỏ hàng thành công", null));
    }

    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<ApiResponse<Void>> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok(new ApiResponse<>("success", "Xóa toàn bộ giỏ hàng thành công", null));
    }
}
