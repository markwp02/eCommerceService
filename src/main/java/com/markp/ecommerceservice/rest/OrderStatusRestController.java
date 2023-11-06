package com.markp.ecommerceservice.rest;

import com.markp.ecommerceservice.entity.OrderStatus;
import com.markp.ecommerceservice.service.OrderStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderStatusRestController {

    @Autowired
    OrderStatusService orderStatusService;

    @GetMapping("/orderStatus")
    public List<OrderStatus> findAll() {
        return orderStatusService.findAll();
    }

    @GetMapping("/orderStatus/{theId}")
    public OrderStatus findById(@PathVariable int theId) {
        return orderStatusService.findById(theId);
    }
}
