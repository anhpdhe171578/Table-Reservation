package com.example.demo.service.impl;

import com.example.demo.dto.CartItemDTO;
import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.OrderItemDTO;
import com.example.demo.entity.CartItem;
import com.example.demo.entity.Dish;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.DishRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private DishRepository dishRepository;

    @Override
    public OrderDTO createOrder(Long userID, Long tableID) {
        List<CartItem> cartItems = cartItemRepository.findByUserId(userID);

        List<OrderItem> orderItems = cartItems.stream()
                .map(c -> OrderItem.builder()
                        .dishId(c.getDishId())
                        .quantity(c.getQuantity())
                        .build())
                .collect(Collectors.toList());

        Order order = Order.builder()
                .userID(userID)
                .tableID(tableID)
                .status("pending")
                .createdAt(LocalDateTime.now())
                .orderItems(orderItems)
                .build();

        order = orderRepository.save(order);

        cartItemRepository.deleteAllByUserId(userID);

        return toDTO(order);
    }

    @Override
    public List<OrderDTO> getOrdersByUser(Long userID) {
        return orderRepository.findByUserID(userID)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getOrdersByTable(Long tableID) {
        return orderRepository.findByTableID(tableID)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO updateOrderItem(Long orderID, Long dishId, int quantity) {
        Order order = orderRepository.findById(orderID).orElse(null);
        if (order == null) return null;

        for (OrderItem item : order.getOrderItems()) {
            if (item.getDishId().equals(dishId)) {
                item.setQuantity(quantity);
                orderRepository.save(order);
                return toDTO(order);
            }
        }
        return null;
    }

    @Override
    public void removeOrderItem(Long orderID, Long dishId) {
        Order order = orderRepository.findById(orderID).orElse(null);
        if (order == null) return;

        order.getOrderItems().removeIf(i -> i.getDishId().equals(dishId));
        orderRepository.save(order);
    }

    private OrderDTO toDTO(Order order) {

        // Map OrderItem → OrderItemDTO
        List<OrderItemDTO> items = order.getOrderItems()
                .stream()
                .map(i -> {
                    double price = dishRepository.findById(i.getDishId())
                            .map(Dish::getPrice)
                            .orElse(0.0);
                    double amount = i.getQuantity() * price;
                    return new OrderItemDTO(i.getDishId(), i.getQuantity(), price, amount);
                })
                .collect(Collectors.toList());

        // Tính tổng tiền
        double totalAmount = items.stream()
                .mapToDouble(OrderItemDTO::getAmount)
                .sum();

        return new OrderDTO(
                order.getOrderID(),
                order.getUserID(),
                order.getTableID(),
                order.getCreatedAt(),
                order.getStatus(),
                items,
                totalAmount
        );
    }
}
