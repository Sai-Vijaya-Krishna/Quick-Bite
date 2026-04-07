package com.example.quickbite.repository;

import com.example.quickbite.entity.Order;
import com.example.quickbite.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByOrderTimeDesc(User user);
    List<Order> findByStatusOrderByOrderTimeDesc(String status);
    long countByStatus(String status);
}
