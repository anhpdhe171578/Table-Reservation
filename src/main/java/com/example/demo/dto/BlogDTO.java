package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogDTO {
    private Long id;           // IdBlog
    private String image;
    private String title;
    private String subTitle;
    private String description;
    private String createAtBy; // người tạo
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
