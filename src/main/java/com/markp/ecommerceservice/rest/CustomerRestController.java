package com.markp.ecommerceservice.rest;

import com.markp.ecommerceservice.dto.CustomerDTO;
import com.markp.ecommerceservice.entity.Customer;
import com.markp.ecommerceservice.dto.LoginResponseDTO;
import com.markp.ecommerceservice.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CustomerRestController {

    @Autowired
    CustomerService customerService;

    @PostMapping("/login")
    public LoginResponseDTO authenticateLogin(@RequestBody CustomerDTO theCustomerDTO) {
        return customerService.authenticateLogin(theCustomerDTO);
    }

    @PostMapping("/signup")
    public boolean signupCustomer(@RequestBody CustomerDTO theCustomerDTO) {
        return customerService.addCustomer(theCustomerDTO);
    }
}
