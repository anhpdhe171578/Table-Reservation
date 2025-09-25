package com.example.demo.service.impl;

import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.OrderItemDTO;
import com.example.demo.entity.CartItem;
import com.example.demo.entity.Dish;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import com.example.demo.entity.TableEntity;
import com.example.demo.entity.User;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.DishRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.TableRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TableRepository tableRepository;

    @Override
    public OrderDTO createOrder(UUID userId, Long tableId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        TableEntity table = tableRepository.findById(tableId).orElseThrow(() -> new RuntimeException("Table not found"));

        List<CartItem> cartItems = cartItemRepository.findByUser(user);

        List<OrderItem> orderItems = cartItems.stream()
                .map(c -> {
                    Dish dish = c.getDish();
                    return OrderItem.builder()
                            .dish(dish)
                            .quantity(c.getQuantity())
                            .build();
                })
                .collect(Collectors.toList());

        Order order = Order.builder()
                .user(user)
                .table(table)
                .status("pending")
                .createdAt(LocalDateTime.now())
                .orderItems(orderItems)
                .build();

        order = orderRepository.save(order);

        // Xóa giỏ hàng sau khi tạo order
        cartItemRepository.deleteAll(cartItems);

        return toDTO(order);
    }

    @Override
    public List<OrderDTO> getOrdersByUser(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return orderRepository.findByUser(user)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getOrdersByTable(Long tableId) {
        TableEntity table = tableRepository.findById(tableId).orElseThrow(() -> new RuntimeException("Table not found"));
        return orderRepository.findByTable(table)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO updateOrderItem(Long orderId, Long dishId, int quantity) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) return null;

        order.getOrderItems().forEach(item -> {
            if (item.getDish().getDishId().equals(dishId)) {
                item.setQuantity(quantity);
            }
        });

        orderRepository.save(order);
        return toDTO(order);
    }

    @Override
    public void removeOrderItem(Long orderId, Long dishId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) return;

        order.getOrderItems().removeIf(i -> i.getDish().getDishId().equals(dishId));
        orderRepository.save(order);
    }

    private OrderDTO toDTO(Order order) {

        List<OrderItemDTO> items = order.getOrderItems()
                .stream()
                .map(i -> {
                    double price = i.getDish().getPrice();
                    double amount = i.getQuantity() * price;
                    return new OrderItemDTO(i.getDish().getDishId(), i.getQuantity(), price, amount);
                })
                .collect(Collectors.toList());

        double totalAmount = items.stream().mapToDouble(OrderItemDTO::getAmount).sum();

        return new OrderDTO(
                order.getOrderID(),
                order.getUser().getId(),
                order.getTable().getTableID(),
                order.getCreatedAt(),
                order.getStatus(),
                items,
                totalAmount
        );
    }
}
