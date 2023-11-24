package com.markp.ecommerceservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="customer")
public class Customer {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="customer_id")
    private int customerId;

    @Column(name="customer_username")
    private String customerUsername;

    @Column(name="customer_password")
    private String customerPassword;

    @Column(name="customer_first_name")
    private String customerFirstName;

    @Column(name="customer_last_name")
    private String customerLastName;

    @Column(name="customer_email")
    private String customerEmail;
}
