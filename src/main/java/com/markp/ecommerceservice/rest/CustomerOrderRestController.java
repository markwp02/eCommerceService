package com.markp.ecommerceservice.rest;

import com.markp.ecommerceservice.entity.CustomerOrder;
import com.markp.ecommerceservice.entity.Product;
import com.markp.ecommerceservice.service.CustomerOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CustomerOrderRestController {

    @Autowired
    CustomerOrderService customerOrderService;

    @GetMapping("/customerOrders")
    public List<CustomerOrder> findAll() {
        return customerOrderService.findAll();
    }

    @GetMapping("/customerOrders/{theId}")
    public CustomerOrder findById(@PathVariable int theId) {
        return customerOrderService.findById(theId);
    }

    @PostMapping("/customerOrders")
    public CustomerOrder addCustomerOrder(@RequestBody CustomerOrder customerOrder) {
        customerOrderService.add(customerOrder);
        return customerOrder;
    }

    @PutMapping("/customerOrders")
    public CustomerOrder updateCustomerOrder(@RequestBody CustomerOrder theCustomerOrder) {
        customerOrderService.update(theCustomerOrder);
        return theCustomerOrder;
    }

    @DeleteMapping("/customerOrders/{theId}")
    public String deleteProduct(@PathVariable int theId) {
        customerOrderService.deleteById(theId);
        return "Deleted customer order id - " + theId;
    }
}
