package com.example.demo.dto;
import lombok.Data;
@Data
public class RegisterRequest {
    private String fullName;
    private String userName;
    private String email;
    private String password;
    private String phoneNumber;
    private String gender;
}
