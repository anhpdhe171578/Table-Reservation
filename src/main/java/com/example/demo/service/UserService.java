package com.example.demo.service;

import com.example.demo.dto.UserDTO;
import com.example.demo.dto.RegisterRequest;
import java.util.List;
import java.util.UUID;

public interface UserService {
    List<UserDTO> getAll();
    UserDTO getById(UUID id);
    UserDTO update(UUID id, RegisterRequest request);
    void delete(UUID id);
}
