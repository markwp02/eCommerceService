package com.markp.ecommerceservice.repository;

import com.markp.ecommerceservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
