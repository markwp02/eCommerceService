package com.markp.ecommerceservice.service;

import com.markp.ecommerceservice.dto.CustomerDTO;
import com.markp.ecommerceservice.entity.Customer;
import com.markp.ecommerceservice.dto.LoginResponseDTO;

public interface CustomerService {

    public LoginResponseDTO authenticateLogin(CustomerDTO theCustomerDTO);

    public boolean addCustomer(CustomerDTO theCustomerDTO);
}
