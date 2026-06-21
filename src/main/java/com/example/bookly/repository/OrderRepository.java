package com.example.bookly.repository;

import com.example.bookly.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
    @Query("SELECT o FROM Order o JOIN FETCH o.orderItems oi JOIN FETCH oi.book WHERE o.id = :id")
    java.util.Optional<Order> findByIdWithItems(Long id);

}
