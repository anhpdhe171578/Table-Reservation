package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long orderID;
    private Long userID;
    private Long tableID;
    private LocalDateTime createdAt;
    private String status;
    private List<OrderItemDTO> orderItems;
    private double totalAmount;
}
