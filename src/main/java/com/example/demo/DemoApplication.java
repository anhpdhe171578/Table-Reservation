package com.example.demo;

import com.example.demo.entity.Role;
import com.example.demo.entity.RoleName;
import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;
import java.util.UUID;

@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    CommandLineRunner initRolesAndAdmin(RoleRepository roleRepository, UserRepository userRepository) {
        return args -> {
            // 1️⃣ Khởi tạo role nếu chưa tồn tại
            for (RoleName roleName : RoleName.values()) {
                if (!roleRepository.existsByName(roleName)) {
                    roleRepository.save(Role.builder().name(roleName).build());
                }
            }

            // 2️⃣ Tạo tài khoản admin nếu chưa có
            if (userRepository.findByUserName("admin").isEmpty()) {
                Role adminRole = roleRepository.findByName(RoleName.ADMIN)
                        .orElseThrow(() -> new RuntimeException("Role ADMIN không tồn tại"));

                User admin = User.builder()
                        .id(UUID.randomUUID())
                        .userName("admin")
                        .fullName("Administrator")
                        .email("admin@example.com")
                        .phoneNumber("0123456789")
                        .gender("1")
                        .passwordHash(new BCryptPasswordEncoder().encode("123456"))
                        .status(true)
                        .roles(Set.of(adminRole))
                        .build();

                userRepository.save(admin);
                System.out.println("✅ Admin account created: username=admin, password=123456");
            }
        };
    }
}
