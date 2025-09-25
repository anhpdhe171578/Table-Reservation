package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderID;

    private Long userID;
    private Long tableID;
    private LocalDateTime createdAt;
    private String status; // ex: pending, confirmed, served

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "orderID")
    private List<OrderItem> orderItems;
}
