package com.markp.ecommerceservice.service;

import com.markp.ecommerceservice.dto.CustomerDTO;
import com.markp.ecommerceservice.entity.Customer;
import com.markp.ecommerceservice.dto.LoginResponseDTO;
import com.markp.ecommerceservice.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService{

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public LoginResponseDTO authenticateLogin(CustomerDTO theCustomerDTO) {
        Optional<Customer> result = customerRepository.findByCustomerUsername(theCustomerDTO.getCustomerUsername());

        if(result.isEmpty()) {
            return new LoginResponseDTO("Username does not exist");
        }

        Customer storedCustomer = result.get();

        String rawPassword = theCustomerDTO.getCustomerPassword();
        String storedPassword = storedCustomer.getCustomerPassword();
        boolean correctPassword = passwordEncoder.matches(rawPassword, storedPassword);
        if(correctPassword) {
            return new LoginResponseDTO(storedCustomer.getCustomerId(), storedCustomer.getCustomerUsername(),
                    storedCustomer.getCustomerFirstName(), storedCustomer.getCustomerLastName(), storedCustomer.getCustomerEmail());
        } else {
            return new LoginResponseDTO("Password is incorrect");
        }
    }

    @Override
    public boolean addCustomer(CustomerDTO theCustomerDTO) {
        Optional<Customer> result = customerRepository.findByCustomerUsername(theCustomerDTO.getCustomerUsername());

        if(result.isPresent()) {
            return false;
        }
        String encodedPassword = passwordEncoder.encode(theCustomerDTO.getCustomerPassword());

        Customer newCustomer = new Customer(
                0,
                theCustomerDTO.getCustomerUsername(),
                encodedPassword,
                theCustomerDTO.getCustomerFirstName(),
                theCustomerDTO.getCustomerLastName(),
                theCustomerDTO.getCustomerEmail());

        customerRepository.save(newCustomer);
        return true;
    }
}
