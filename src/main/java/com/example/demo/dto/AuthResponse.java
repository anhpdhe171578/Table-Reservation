package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class AuthResponse {
    private String token;
    private String username;
    private String fullName;
    private LocalDate dob;
    private String email;
    private String phoneNumber;
    private String gender;
    private List<String> role;
}
