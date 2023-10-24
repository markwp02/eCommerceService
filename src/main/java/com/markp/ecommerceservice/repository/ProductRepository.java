package com.markp.ecommerceservice.repository;

import com.markp.ecommerceservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByProductCategory(String productCategory);
    List<Product> findByProductNameContainingIgnoreCase(String productName);

    @Query("SELECT distinct productCategory from Product")
    List<String> findProductCategories();
}
