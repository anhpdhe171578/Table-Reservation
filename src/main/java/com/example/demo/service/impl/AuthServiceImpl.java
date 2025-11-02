package com.example.demo.service.impl;

import com.example.demo.config.JwtUtil;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.entity.Role;
import com.example.demo.entity.RoleName;
import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public AuthResponse register(RegisterRequest request) {
        // 🔸 Kiểm tra username tồn tại
        if (userRepository.findByUserName(request.getUserName()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Username already exists"
            );
        }

        //Kiểm tra email
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Email already exists"
            );
        }

        //Lấy role
        Role customerRole = roleRepository.findByName(RoleName.CUSTOMER)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role CUSTOMER not found"));

        //Tạo user mới và gán role
        User user = User.builder()
                .id(UUID.randomUUID())
                .fullName(request.getFullName())
                .userName(request.getUserName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .gender(request.getGender())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .status(true)
                .roles(Set.of(customerRole))
                .build();

        userRepository.save(user);

        //Tạo JWT token
        List<String> roles = user.getRoles()
                .stream()
                .map(role -> "ROLE_" + role.getName().name())
                .toList();

        String token = jwtUtil.generateToken(user.getId(), user.getUserName(), roles);

        List<String> rolesForDto = user.getRoles()
                .stream()
                .map(role -> role.getName().name())
                .toList();
        return AuthResponse.builder()
                .token(token)
                .username(user.getUserName())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(rolesForDto)
                .phoneNumber(user.getPhoneNumber())
                .gender(user.getGender())
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Optional<User> userOpt = userRepository.findByUserName(request.getUserName());
        if (userOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        User user = userOpt.get();
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        List<String> roles = user.getRoles() // chuyển sang List<String>
                .stream()
                .map(role -> "ROLE_" + role.getName().name())
                .toList();

        String token = jwtUtil.generateToken(user.getId(), user.getUserName(), roles);
        List<String> rolesForDto = user.getRoles()
                .stream()
                .map(role -> role.getName().name())
                .toList();
        return AuthResponse.builder()
                .token(token)
                .username(user.getUserName())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(rolesForDto)
                .phoneNumber(user.getPhoneNumber())
                .gender(user.getGender())
                .build();
    }
}
