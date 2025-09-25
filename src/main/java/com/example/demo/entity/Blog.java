package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "[Blogs]")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBlog;
    private String image;
    private String title;
    private String subTitle;
    private String description;
    private String createAtBy;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
