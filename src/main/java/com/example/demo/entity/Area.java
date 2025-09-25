package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "[Areas]")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long areaID;

    private String areaName;
    private Long restaurantID; // liên kết với Restaurant
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
