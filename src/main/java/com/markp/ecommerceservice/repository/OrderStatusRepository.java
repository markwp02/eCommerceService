package com.markp.ecommerceservice.repository;

import com.markp.ecommerceservice.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Integer> {
}
