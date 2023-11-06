package com.markp.ecommerceservice.service;

import com.markp.ecommerceservice.entity.OrderProduct;

import java.util.List;

public interface OrderProductService {

    public List<OrderProduct> findAll();

    public OrderProduct findByOrderProductId(int theOrderProductId);

    public void add(OrderProduct theOrderProduct);

    public void update(OrderProduct theOrderProduct);

    public void deleteById(int theId);
}
