package com.markp.ecommerceservice.repository;

import com.markp.ecommerceservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByProductCategory(String productCategory);
    List<Product> findByProductNameContainingIgnoreCase(String productName);
}
