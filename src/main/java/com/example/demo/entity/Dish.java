package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "[Dishs]")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dishId;

    private String name;
    private Double price;
    private String description;

    private Long categoryID; // Nếu bạn muốn liên kết Category, có thể dùng @ManyToOne
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String image;
    private String type;
}
