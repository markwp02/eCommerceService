package com.markp.ecommerceservice.repository;

import com.markp.ecommerceservice.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    public Optional<Customer> findByCustomerUsername(String customerUsername);
}
