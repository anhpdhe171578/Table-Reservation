package com.example.demo.repository;

import com.example.demo.entity.Order;
import com.example.demo.entity.TableEntity;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Lấy danh sách order theo User
    List<Order> findByUser(User user);

    // Lấy danh sách order theo Table
    List<Order> findByTable(TableEntity table);
}
