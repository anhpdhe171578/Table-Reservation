package com.example.demo.repository;

import com.example.demo.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // Lấy tất cả món trong giỏ của user
    List<CartItem> findByUserId(Long userId);

    // Tìm món cụ thể trong giỏ của user
    CartItem findByUserIdAndDishId(Long userId, Long dishId);

    // Xóa toàn bộ giỏ hàng của user
    void deleteAllByUserId(Long userId);
}
