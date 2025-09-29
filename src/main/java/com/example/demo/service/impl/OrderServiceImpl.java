package com.example.demo.service.impl;

import com.example.demo.dto.DishDTO;
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
    private OrderRepository orderRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TableRepository tableRepository;

    // ===== Customer đặt bàn từ Cart =====
    @Override
    public OrderDTO createOrderFromCart(UUID userId, Long tableId, LocalDateTime reservationTime) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Table not found"));
        TableEntity table = tableRepository.findById(tableId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Table not found"));

        List<CartItem> cartItems = cartItemRepository.findByUser(user);

        List<OrderItem> orderItems = cartItems.stream()
                .map(c -> OrderItem.builder()
                        .dish(c.getDish())
                        .quantity(c.getQuantity())
                        .build())
                .collect(Collectors.toList());

        Order order = Order.builder()
                .user(user)
                .table(table)
                .status("pending")
                .createdAt(LocalDateTime.now())
                .reservationTime(reservationTime)
                .orderItems(orderItems)
                .build();

        order = orderRepository.save(order);

        cartItemRepository.deleteAll(cartItems);

        return toDTO(order);
    }

    @Override
    public OrderDTO createOrderByReceptionist(Long tableId) {
        TableEntity table = tableRepository.findById(tableId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Table not found"));

        // Tạo order mới, chưa có món
        Order order = Order.builder()
                .table(table)
                .status("pending")
                .createdAt(LocalDateTime.now())
                .reservationTime(LocalDateTime.now())
                .orderItems(new ArrayList<>()) // chưa có món
                .build();

        order = orderRepository.save(order);
        return toDTO(order);
    }



    @Override
    public OrderDTO addDishToTable(Long tableId, Long dishId, int quantity) {
        TableEntity table = tableRepository.findById(tableId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Table not found"));

        // tìm order "pending" cho table
        Order order = orderRepository.findByTableAndStatus(table, "pending")
                .orElse(null);

        if (order == null) {
            order = Order.builder()
                    .table(table)
                    .status("pending")
                    .createdAt(LocalDateTime.now())
                    .build();
        }

        Dish dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Table not found"));

        // kiểm tra xem món đã có trong order chưa
        OrderItem existingItem = order.getOrderItems()
                .stream()
                .filter(i -> i.getDish().getDishId().equals(dishId))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            OrderItem newItem = OrderItem.builder()
                    .dish(dish)
                    .quantity(quantity)
                    .build();
            order.getOrderItems().add(newItem);
        }

        orderRepository.save(order);

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
        TableEntity table = tableRepository.findById(tableId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Table not found"));
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
                    Dish dish = i.getDish();
                    double price = dish.getPrice();
                    double amount = i.getQuantity() * price;

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
                            i.getId(),
                            dishDTO,
                            i.getQuantity(),
                            price,
                            amount
                    );
                })
                .collect(Collectors.toList());

        double totalAmount = items.stream().mapToDouble(OrderItemDTO::getAmount).sum();

        return new OrderDTO(
                order.getOrderID(),
                order.getUser().getId(),
                order.getTable().getTableID(),
                order.getCreatedAt(),
                order.getReservationTime(),
                order.getStatus(),
                items,
                totalAmount
        );
    }
}
