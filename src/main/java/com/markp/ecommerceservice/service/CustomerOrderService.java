package com.markp.ecommerceservice.service;

import com.markp.ecommerceservice.entity.CustomerOrder;

import java.util.List;

public interface CustomerOrderService {

    public List<CustomerOrder> findAll();

    public CustomerOrder findById(int theId);

    public void add(CustomerOrder theCustomerOrder);

    public void update(CustomerOrder theCustomerOrder);

    public void deleteById(int theId);
}
