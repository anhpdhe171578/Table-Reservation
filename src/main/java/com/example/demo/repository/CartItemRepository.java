package com.example.demo.repository;

import com.example.demo.entity.CartItem;
import com.example.demo.entity.Dish;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByUser(User user);

    CartItem findByUserAndDish(User user, Dish dish);

    void deleteAllByUser(User user);
}
