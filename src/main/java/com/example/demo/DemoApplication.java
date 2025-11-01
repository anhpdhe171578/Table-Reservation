package com.example.demo;

import com.example.demo.entity.Role;
import com.example.demo.entity.RoleName;
import com.example.demo.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            for (RoleName roleName : RoleName.values()) {
                if (!roleRepository.existsByName(roleName)) {
                    roleRepository.save(Role.builder().name(roleName).build());
                }
            }
        };
    }
}
