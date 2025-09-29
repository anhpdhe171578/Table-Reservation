package com.example.demo.service;

import com.example.demo.dto.OrderDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface OrderService {

    OrderDTO createOrderFromCart(UUID userId, Long tableId, LocalDateTime reservationTime);
    OrderDTO createOrderByReceptionist(Long tableId) ;

    List<OrderDTO> getOrdersByUser(UUID userId);

    List<OrderDTO> getOrdersByTable(Long tableId);

    OrderDTO updateOrderItem(Long orderId, Long dishId, int quantity);

    void removeOrderItem(Long orderId, Long dishId);

    OrderDTO addDishToTable(Long tableId, Long dishId, int quantity);
}
