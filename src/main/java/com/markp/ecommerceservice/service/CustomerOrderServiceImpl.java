package com.markp.ecommerceservice.service;

import com.markp.ecommerceservice.entity.CustomerOrder;
import com.markp.ecommerceservice.entity.OrderProduct;
import com.markp.ecommerceservice.entity.Product;
import com.markp.ecommerceservice.repository.CustomerOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerOrderServiceImpl implements CustomerOrderService {

    @Autowired
    CustomerOrderRepository customerOrderRepository;

    @Autowired
    OrderStatusService orderStatusService;

    @Autowired
    ProductService productService;

    @Override
    public List<CustomerOrder> findAll() {
        return customerOrderRepository.findAll();
    }

    @Override
    public CustomerOrder findById(int theId) {
        Optional<CustomerOrder> result = customerOrderRepository.findById(theId);

        CustomerOrder customerOrder;
        if(result.isPresent()) {
            customerOrder = result.get();
        } else {
            throw new RuntimeException("Did not find customer order with id - " + theId);
        }
        return customerOrder;
    }

    /**
     * Separate methods to add a new customer order. Setting the primary key
     * customerOrderId to 0 will force the save of a new item in case a
     * customerOrderId is passed in the CustomerOrder object.
     *
     * As join between OrderProduct and Product has a CascadeType of Merge, only
     * an update database operation will change the product stock.
     * @param theCustomerOrder
     */
    @Override
    public void add(CustomerOrder theCustomerOrder) {
        theCustomerOrder.setCustomerOrderId(0);
        customerOrderRepository.save(theCustomerOrder);

        boolean updateStockListResult = theCustomerOrder.updateProductStockList();

        setOrderStatus(theCustomerOrder, updateStockListResult);

        update(theCustomerOrder);
    }

    @Override
    public void update(CustomerOrder theCustomerOrder) {
        customerOrderRepository.save(theCustomerOrder);
    }

    @Override
    public void deleteById(int theId) {
        Optional<CustomerOrder> result = customerOrderRepository.findById(theId);

        if(result.isEmpty()) {
            throw new RuntimeException("Customer order not found - " + theId);
        } else {
            customerOrderRepository.deleteById(theId);
        }
    }

    private void setOrderStatus(CustomerOrder theCustomerOrder, boolean success) {
        int confirmedId = 1;
        int rejectedId = 5;
        int statusId = success ? confirmedId : rejectedId;
        theCustomerOrder.setOrderStatus(orderStatusService.findById(statusId));
    }
}
