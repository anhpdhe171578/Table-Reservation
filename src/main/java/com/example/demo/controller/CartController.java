package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.CartItemDTO;
import com.example.demo.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<ApiResponse<List<CartItemDTO>>> getCart(@PathVariable UUID userId) {
        List<CartItemDTO> cart = cartService.getCartByUser(userId);
        return ResponseEntity.ok(new ApiResponse<>("success", "Lấy giỏ hàng thành công", cart));
    }

    @PostMapping("/{userId}/add")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<ApiResponse<CartItemDTO>> addToCart(
            @PathVariable UUID userId,
            @RequestParam Long dishId,
            @RequestParam int quantity) {
        CartItemDTO item = cartService.addToCart(userId, dishId, quantity);
        return ResponseEntity.ok(new ApiResponse<>("success", "Thêm món vào giỏ hàng thành công", item));
    }

    @PutMapping("/{userId}/update")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<ApiResponse<CartItemDTO>> updateCart(
            @PathVariable UUID userId,
            @RequestParam Long dishId,
            @RequestParam int quantity) {
        CartItemDTO item = cartService.updateQuantity(userId, dishId, quantity);
        if (item == null)
            return ResponseEntity.ok(new ApiResponse<>("error", "Món không tồn tại trong giỏ hàng", null));
        return ResponseEntity.ok(new ApiResponse<>("success", "Cập nhật số lượng thành công", item));
    }

    @DeleteMapping("/{userId}/remove")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<ApiResponse<Void>> removeFromCart(
            @PathVariable UUID userId,
            @RequestParam Long dishId) {
        cartService.removeFromCart(userId, dishId);
        return ResponseEntity.ok(new ApiResponse<>("success", "Xóa món khỏi giỏ hàng thành công", null));
    }

    @DeleteMapping("/{userId}/clear")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<ApiResponse<Void>> clearCart(@PathVariable UUID userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok(new ApiResponse<>("success", "Xóa toàn bộ giỏ hàng thành công", null));
    }
}
