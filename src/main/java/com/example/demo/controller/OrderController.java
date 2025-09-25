package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.OrderDTO;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<OrderDTO>> createOrder(
            @RequestParam UUID userID,
            @RequestParam Long tableID) {
        OrderDTO order = orderService.createOrder(userID, tableID);
        return ResponseEntity.ok(new ApiResponse<>("success", "Đặt món thành công", order));
    }

    @GetMapping("/user/{userID}")
    public ResponseEntity<ApiResponse<List<OrderDTO>>> getOrdersByUser(@PathVariable UUID userID) {
        List<OrderDTO> orders = orderService.getOrdersByUser(userID);
        return ResponseEntity.ok(new ApiResponse<>("success", "Danh sách đơn hàng của user", orders));
    }

    @GetMapping("/table/{tableID}")
    public ResponseEntity<ApiResponse<List<OrderDTO>>> getOrdersByTable(@PathVariable Long tableID) {
        List<OrderDTO> orders = orderService.getOrdersByTable(tableID);
        return ResponseEntity.ok(new ApiResponse<>("success", "Danh sách đơn hàng của bàn", orders));
    }

    @PutMapping("/{orderID}/update-item")
    public ResponseEntity<ApiResponse<OrderDTO>> updateOrderItem(
            @PathVariable Long orderID,
            @RequestParam Long dishId,
            @RequestParam int quantity) {
        OrderDTO updated = orderService.updateOrderItem(orderID, dishId, quantity);
        if (updated == null)
            return ResponseEntity.ok(new ApiResponse<>("error", "Không tìm thấy món trong đơn hàng", null));
        return ResponseEntity.ok(new ApiResponse<>("success", "Cập nhật món thành công", updated));
    }

    @DeleteMapping("/{orderID}/remove-item")
    public ResponseEntity<ApiResponse<Void>> removeOrderItem(
            @PathVariable Long orderID,
            @RequestParam Long dishId) {
        orderService.removeOrderItem(orderID, dishId);
        return ResponseEntity.ok(new ApiResponse<>("success", "Xóa món khỏi đơn hàng thành công", null));
    }
}
