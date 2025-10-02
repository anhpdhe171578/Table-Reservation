package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.OrderDTO;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/customer/create")
    public ResponseEntity<OrderDTO> createOrderForCustomer(
            @RequestParam UUID userId,
            @RequestParam Long tableId,
            @RequestParam String reservationTime  // từ client gửi string ISO, vd: 2025-09-30T19:30
    ) {
        LocalDateTime resTime = LocalDateTime.parse(reservationTime);
        return ResponseEntity.ok(orderService.createOrderFromCart(userId, tableId, resTime));
    }

    @PostMapping("/receptionist/create")
    public ResponseEntity<OrderDTO> createOrderForReceptionist(@RequestParam Long tableId) {
        return ResponseEntity.ok(orderService.createOrderByReceptionist(tableId));
    }

    @PostMapping("/tables/{tableId}/add-dish")
    public ResponseEntity<OrderDTO> addDishToTable(
            @PathVariable Long tableId,
            @RequestParam Long dishId,
            @RequestParam int quantity
    ) {
        return ResponseEntity.ok(orderService.addDishToTable(tableId, dishId, quantity));
    }

    @GetMapping
    public List<OrderDTO> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/user/{userID}")
    public ResponseEntity<ApiResponse<List<OrderDTO>>> getOrdersByUser(
            @PathVariable UUID userID) {
        List<OrderDTO> orders = orderService.getOrdersByUser(userID);
        return ResponseEntity.ok(new ApiResponse<>("success", "Danh sách đơn hàng của user", orders));
    }

    @GetMapping("/table/{tableID}")
    public ResponseEntity<ApiResponse<List<OrderDTO>>> getOrdersByTable(
            @PathVariable Long tableID) {
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
