package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "AppUsers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @Column(name = "id", columnDefinition = "uniqueidentifier")
    private UUID id;
    private String fullName;
    private LocalDate dob;
    private String refreshToken;
    private Long restaurantID;
    private Boolean status;
    private String userName;
    private String email;
    private Boolean emailConfirmed;
    private String passwordHash;
    private String phoneNumber;
    private Boolean phoneNumberConfirmed;
    private String gender;
}
