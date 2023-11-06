package com.markp.ecommerceservice.rest;

import com.markp.ecommerceservice.entity.OrderProduct;
import com.markp.ecommerceservice.service.OrderProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderProductRestController {

    @Autowired
    OrderProductService orderProductService;

    @GetMapping("/orderProducts")
    public List<OrderProduct> findAll() {
        return orderProductService.findAll();
    }

    @GetMapping("/orderProducts/{orderProductId}")
    public OrderProduct findByOrderProductId(@PathVariable int orderProductId) {
        return orderProductService.findByOrderProductId(orderProductId);
    }

}
