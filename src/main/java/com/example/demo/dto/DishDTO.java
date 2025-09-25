package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishDTO {
    private Long id;          // dishId
    private String name;
    private Double price;
    private String description;
    private String image;
    private String type;
    private Long categoryId;
}

