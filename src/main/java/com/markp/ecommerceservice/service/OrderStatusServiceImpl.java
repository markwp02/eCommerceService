package com.markp.ecommerceservice.service;

import com.markp.ecommerceservice.entity.OrderStatus;
import com.markp.ecommerceservice.repository.OrderStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderStatusServiceImpl implements OrderStatusService {

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    @Override
    public List<OrderStatus> findAll() {
        return orderStatusRepository.findAll();
    }

    @Override
    public OrderStatus findById(int theId) {
        Optional<OrderStatus> result = orderStatusRepository.findById(theId);

        OrderStatus orderStatus;
        if(result.isPresent()) {
            orderStatus = result.get();
        } else {
            throw new RuntimeException("Did not find order status with id - " + theId);
        }
        return orderStatus;
    }
}
