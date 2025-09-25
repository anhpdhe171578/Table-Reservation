package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "[Tables]")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tableID;

    private String tableNumber;
    private String status; // ví dụ: available, occupied
    private Long areaID;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int numberOfDesk; // số lượng chỗ ngồi
}
