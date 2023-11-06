package com.markp.ecommerceservice.repository;

import com.markp.ecommerceservice.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Integer> {

}
