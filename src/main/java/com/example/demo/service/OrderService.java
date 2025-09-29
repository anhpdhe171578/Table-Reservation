package com.example.demo.service;

import com.example.demo.dto.OrderDTO;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    OrderDTO createOrder(UUID userId, Long tableId);

    List<OrderDTO> getOrdersByUser(UUID userId);

    List<OrderDTO> getOrdersByTable(Long tableId);

    OrderDTO updateOrderItem(Long orderId, Long dishId, int quantity);

    void removeOrderItem(Long orderId, Long dishId);

    OrderDTO addDishToTable(Long tableId, Long dishId, int quantity);
}
