package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AreaDTO {
    private Long id; // areaID
    private String areaName;
    private Long restaurantID;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
