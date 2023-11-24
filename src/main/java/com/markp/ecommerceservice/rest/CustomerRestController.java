package com.markp.ecommerceservice.rest;

import com.markp.ecommerceservice.entity.Customer;
import com.markp.ecommerceservice.entity.LoginResponse;
import com.markp.ecommerceservice.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CustomerRestController {

    @Autowired
    CustomerService customerService;

    @PostMapping("/login")
    public LoginResponse authenticateLogin(@RequestBody Customer theCustomer) {
        return customerService.authenticateLogin(theCustomer);
    }

    @PostMapping("/signup")
    public boolean signupCustomer(@RequestBody Customer theCustomer) {
        return customerService.addCustomer(theCustomer);
    }
}
