package com.markp.ecommerceservice.service;

import com.markp.ecommerceservice.entity.OrderStatus;

import java.util.List;

public interface OrderStatusService {

    public List<OrderStatus> findAll();

    public OrderStatus findById(int theId);

}
