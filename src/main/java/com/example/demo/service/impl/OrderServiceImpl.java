package com.example.demo.service.impl;

import com.example.demo.dto.*;
import com.example.demo.entity.Dish;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import com.example.demo.entity.TableEntity;
import com.example.demo.entity.User;
import com.example.demo.service.CartService;
import com.example.demo.service.DishService;
import com.example.demo.service.OrderService;
import com.example.demo.service.TableService;
import com.example.demo.service.UserService;
import com.example.demo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private CartService cartService;

    @Autowired
    private DishService dishService;

    @Autowired
    private UserService userService;

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderRepository orderRepository;

    // ===== Customer đặt bàn từ Cart =====
    @Override
    public OrderDTO createOrderFromCart(UUID userId, Long tableId, LocalDateTime reservationTime) {
        UserDTO userDTO = userService.getById(userId);
        if (userDTO == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");

        TableDTO tableDTO = tableService.getTableById(tableId);
        if (tableDTO == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Table not found");

        List<CartItemDTO> cartItems = cartService.getCartByUser(userId);
        if (cartItems.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cart is empty");

        List<OrderItem> orderItems = cartItems.stream().map(c -> {
            DishDTO dishDTO = dishService.getDishById(c.getDishId());
            Dish dish = new Dish();
            dish.setDishId(dishDTO.getId());
            return OrderItem.builder()
                    .dish(dish)
                    .quantity(c.getQuantity())
                    .build();
        }).collect(Collectors.toList());

        User user = new User();
        user.setId(userDTO.getId());

        TableEntity table = new TableEntity();
        table.setTableID(tableDTO.getId());

        Order order = Order.builder()
                .user(user)
                .table(table)
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .reservationTime(reservationTime)
                .orderItems(orderItems)
                .build();

        order = orderRepository.save(order);

        cartService.clearCart(userId);

        return toDTO(order);
    }

    // ===== Tạo order trống từ receptionist =====
    @Override
    public OrderDTO createOrderByReceptionist(Long tableId) {
        TableDTO tableDTO = tableService.getTableById(tableId);
        if (tableDTO == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Table not found");

        TableEntity table = new TableEntity();
        table.setTableID(tableDTO.getId());

        Order order = Order.builder()
                .table(table)
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .reservationTime(LocalDateTime.now())
                .orderItems(new ArrayList<>())
                .build();

        order = orderRepository.save(order);
        return toDTO(order);
    }

    // ===== Thêm món vào order của table =====
    @Override
    public OrderDTO addDishToTable(Long tableId, Long dishId, int quantity) {
        if (quantity <= 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity must be positive");

        TableDTO tableDTO = tableService.getTableById(tableId);
        if (tableDTO == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Table not found");

        TableEntity table = new TableEntity();
        table.setTableID(tableDTO.getId());

        Order order = orderRepository.findByTableAndStatus(table, "CONFIRMED")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No active order on this table"));


        DishDTO dishDTO = dishService.getDishById(dishId);
        if (dishDTO == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dish not found");

        OrderItem existing = order.getOrderItems().stream()
                .filter(i -> i.getDish().getDishId().equals(dishId))
                .findFirst()
                .orElse(null);

        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + quantity);
        } else {
            Dish dish = new Dish();
            dish.setDishId(dishId);
            OrderItem newItem = OrderItem.builder()
                    .dish(dish)
                    .quantity(quantity)
                    .build();
            order.getOrderItems().add(newItem);
        }

        order = orderRepository.save(order);
        return toDTO(order);
    }

    // ===== Lấy tất cả order =====
    @Override
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ===== Lấy order theo user =====
    @Override
    public List<OrderDTO> getOrdersByUser(UUID userId) {
        UserDTO userDTO = userService.getById(userId);
        if (userDTO == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");

        User user = new User();
        user.setId(userDTO.getId());

        return orderRepository.findByUser(user).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ===== Lấy order theo table =====
    @Override
    public List<OrderDTO> getOrdersByTable(Long tableId) {
        TableDTO tableDTO = tableService.getTableById(tableId);
        if (tableDTO == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Table not found");

        TableEntity table = new TableEntity();
        table.setTableID(tableDTO.getId());

        return orderRepository.findByTable(table).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ===== Cập nhật quantity order item =====
    @Override
    public OrderDTO updateOrderItem(Long orderId, Long dishId, int quantity) {
        if (quantity <= 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity must be positive");

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        order.getOrderItems().forEach(item -> {
            if (item.getDish().getDishId().equals(dishId)) {
                item.setQuantity(quantity);
            }
        });

        order = orderRepository.save(order);
        return toDTO(order);
    }

    // ===== Xóa order item =====
    @Override
    public void removeOrderItem(Long orderId, Long dishId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        order.getOrderItems().removeIf(i -> i.getDish().getDishId().equals(dishId));
        orderRepository.save(order);
    }

    // ===== Mapping helper =====
    private OrderItemDTO toOrderItemDTO(OrderItem item) {
        Dish dish = item.getDish();
        double price = dish.getPrice();
        double amount = price * item.getQuantity();

        DishDTO dishDTO = new DishDTO(
                dish.getDishId(),
                dish.getName(),
                dish.getPrice(),
                dish.getDescription(),
                dish.getImage(),
                dish.getType(),
                dish.getCategory() != null ? dish.getCategory().getCategoryID() : null
        );

        return new OrderItemDTO(
                item.getId(),
                dishDTO,
                item.getQuantity(),
                price,
                amount
        );
    }

    private OrderDTO toDTO(Order order) {
        List<OrderItemDTO> items = order.getOrderItems().stream()
                .map(this::toOrderItemDTO)
                .collect(Collectors.toList());

        double totalAmount = items.stream().mapToDouble(OrderItemDTO::getAmount).sum();

        return new OrderDTO(
                order.getOrderID(),
                order.getUser() != null ? order.getUser().getId() : null,
                order.getTable() != null ? order.getTable().getTableID() : null,
                order.getCreatedAt(),
                order.getReservationTime(),
                order.getStatus(),
                items,
                totalAmount
        );
    }
}
