package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableDTO {
    private Long id; // tableID
    private String tableNumber;
    private String status;
    private Long areaID;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int numberOfDesk;
}
