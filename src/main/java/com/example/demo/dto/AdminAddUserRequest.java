package com.example.demo.dto;

import com.example.demo.entity.RoleName;
import lombok.Data;

@Data
public class AdminAddUserRequest {
    private String fullName;
    private String email;
    private String phoneNumber;
    private String userName;
    private String password;
    private RoleName roleName;
    private String gender;
}
