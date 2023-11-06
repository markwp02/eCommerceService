package com.markp.ecommerceservice.service;

import com.markp.ecommerceservice.entity.OrderProduct;
import com.markp.ecommerceservice.repository.OrderProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderProductServiceImpl implements OrderProductService {

    @Autowired
    OrderProductRepository orderProductRepository;

    @Override
    public List<OrderProduct> findAll() {
        return orderProductRepository.findAll();
    }

    @Override
    public OrderProduct findByOrderProductId(int theOrderProductId) {
        Optional<OrderProduct> result = orderProductRepository.findById(theOrderProductId);

        OrderProduct orderProduct;
        if(result.isPresent()) {
            orderProduct = result.get();
        } else {
            throw new RuntimeException("Did not find order product with id - " + theOrderProductId);
        }
        return orderProduct;
    }

    /**
     * Separate methods to add a new order product. Setting the primary key
     * orderProductId to 0 will force the save of a new item in case a
     * orderProductId is passed in the OrderProduct object.
     * @param theOrderProduct
     */
    @Override
    public void add(OrderProduct theOrderProduct) {
        theOrderProduct.setOrderProductId(0);
        orderProductRepository.save(theOrderProduct);
    }

    @Override
    public void update(OrderProduct theOrderProduct) {
        orderProductRepository.save(theOrderProduct);
    }

    @Override
    public void deleteById(int theId) {
        Optional<OrderProduct> result = orderProductRepository.findById(theId);

        if(result.isEmpty()) {
            throw new RuntimeException("Order Product not found - " + theId);
        } else {
            orderProductRepository.deleteById(theId);
        }
    }
}
