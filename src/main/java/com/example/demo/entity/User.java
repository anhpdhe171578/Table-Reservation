package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "app_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @Column(columnDefinition = "uniqueidentifier")
    private UUID id;

    private String fullName;
    private LocalDate dob;
    private String userName;
    private String email;
    private Boolean emailConfirmed;
    private String passwordHash;
    private String phoneNumber;
    private Boolean phoneNumberConfirmed;
    private String gender;
    private Boolean status;
    private String refreshToken;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<AppRole> roles;

    @OneToMany(mappedBy = "user")
    private Set<CartItem> cartItems;

    @OneToMany(mappedBy = "user")
    private Set<Order> orders;

    @ManyToOne
    @JoinColumn(name = "restaurantID")
    private Restaurant restaurant;
}
