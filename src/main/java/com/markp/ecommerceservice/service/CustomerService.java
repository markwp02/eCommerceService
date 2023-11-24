package com.markp.ecommerceservice.service;

import com.markp.ecommerceservice.entity.Customer;
import com.markp.ecommerceservice.entity.LoginResponse;

public interface CustomerService {

    public LoginResponse authenticateLogin(Customer theCustomer);

    public boolean addCustomer(Customer theCustomer);
}
