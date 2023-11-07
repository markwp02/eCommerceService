package com.markp.ecommerceservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="customer_order")
public class CustomerOrder {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="customer_order_id")
    private int customerOrderId;

    @Column(name="customer_order_total_price")
    private BigDecimal customerOrderTotalPrice;

    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name="customer_order_id")
    private List<OrderProduct> orderProducts;

    @ManyToOne(cascade=CascadeType.REFRESH)
    @JoinColumn(name="order_status_id")
    private OrderStatus orderStatus;

    public boolean updateProductStockList() {
        List<OrderProduct> successOrderProducts = new ArrayList<>();
        List<OrderProduct> failureOrderProducts = new ArrayList<>();
        boolean productStockResult;
        boolean productStockListResult = true;

        for(OrderProduct orderProduct: orderProducts) {
            productStockResult = orderProduct.removeStockFromProduct();
            if(!productStockResult) {
                failureOrderProducts.add(orderProduct);
                productStockListResult = false;
            } else {
                successOrderProducts.add(orderProduct);
            }
        }

        if(failureOrderProducts.size() > 0) {
            rollbackProductStockList(successOrderProducts);
        }
        return productStockListResult;
    }

    private void rollbackProductStockList(List<OrderProduct> orderProductsToRollback) {
        for(OrderProduct orderProduct: orderProductsToRollback) {
            orderProduct.returnStockFromProduct();
        }
    }
}
