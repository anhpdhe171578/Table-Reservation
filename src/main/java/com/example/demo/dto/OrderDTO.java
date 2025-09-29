package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long orderID;
    private UUID userId;
    private Long tableID;
    private LocalDateTime createdAt;
    private LocalDateTime reservationTime;
    private String status;
    private List<OrderItemDTO> orderItems;
    private double totalAmount;
}
