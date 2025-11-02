package com.example.demo.service.impl;

import com.example.demo.dto.AdminAddUserRequest;
import com.example.demo.dto.UserDTO;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.Role;
import com.example.demo.entity.RoleName;
import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public List<UserDTO> getAll() {
        return userRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getById(UUID id) {
        return userRepository.findById(id)
                .map(this::toDTO)
                .orElse(null);
    }

    @Override
    public UserDTO update(UUID id, RegisterRequest request) {
        return userRepository.findById(id).map(user -> {
            user.setFullName(request.getFullName());
            user.setEmail(request.getEmail());
            user.setPhoneNumber(request.getPhoneNumber());
            user.setUserName(request.getUserName());
            user = userRepository.save(user);
            return toDTO(user);
        }).orElse(null);
    }

    @Override
    public void delete(UUID id) {
        userRepository.deleteById(id);
    }

    private UserDTO toDTO(User user) {
        List<String> roles = user.getRoles()
                .stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toList());
        return UserDTO.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .userName(user.getUserName())
                .email(user.getEmail())
                .role(roles)
                .phoneNumber(user.getPhoneNumber())
                .status(user.isStatus() ? "active" : "inactive")
                .build();
    }

    @Override
    public UserDTO addUserByAdmin(AdminAddUserRequest request) {
        if (userRepository.findByUserName(request.getUserName()).isPresent()) {
            throw new RuntimeException("Username already exists!");
        }

        // 🔹 Xử lý role (Admin nhập vào)
        String inputRole = request.getRoleName() != null ? request.getRoleName().toString().toUpperCase() : "CUSTOMER";
        RoleName roleName;
        try {
            roleName = RoleName.valueOf(inputRole);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role name: " + inputRole);
        }

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));


        User user = User.builder()
                .id(UUID.randomUUID())
                .fullName(request.getFullName())
                .userName(request.getUserName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .gender(request.getGender())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .status(true)
                .roles(Set.of(role))
                .build();

        userRepository.save(user);

        return toDTO(user);
    }

}