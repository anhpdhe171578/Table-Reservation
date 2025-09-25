package com.example.demo.service;

import com.example.demo.dto.OrderDTO;
import java.util.List;

public interface OrderService {
    OrderDTO createOrder(Long userID, Long tableID);
    List<OrderDTO> getOrdersByUser(Long userID);
    List<OrderDTO> getOrdersByTable(Long tableID);
    OrderDTO updateOrderItem(Long orderID, Long dishId, int quantity);
    void removeOrderItem(Long orderID, Long dishId);
}
