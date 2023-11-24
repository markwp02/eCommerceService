package com.markp.ecommerceservice.service;

import com.markp.ecommerceservice.entity.Customer;
import com.markp.ecommerceservice.entity.LoginResponse;
import com.markp.ecommerceservice.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService{

    @Autowired
    CustomerRepository customerRepository;

    @Override
    public LoginResponse authenticateLogin(Customer theCustomer) {
        Optional<Customer> result = customerRepository.findByCustomerUsername(theCustomer.getCustomerUsername());

        if(result.isEmpty()) {
            return new LoginResponse("Username does not exist");
        }

        Customer storedCustomer = result.get();
        boolean correctPassword = theCustomer.getCustomerPassword().equals(storedCustomer.getCustomerPassword());
        if(correctPassword) {
            return new LoginResponse(storedCustomer.getCustomerId(), storedCustomer.getCustomerUsername(),
                    storedCustomer.getCustomerFirstName(), storedCustomer.getCustomerLastName(), storedCustomer.getCustomerEmail());
        } else {
            return new LoginResponse("Password is incorrect");
        }
    }

    @Override
    public boolean addCustomer(Customer theCustomer) {
        Optional<Customer> result = customerRepository.findByCustomerUsername(theCustomer.getCustomerUsername());

        if(result.isPresent()) {
            return false;
        }
        theCustomer.setCustomerId(0);
        customerRepository.save(theCustomer);
        return true;
    }
}
