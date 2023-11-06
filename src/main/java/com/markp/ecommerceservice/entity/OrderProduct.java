package com.markp.ecommerceservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="order_product")
public class OrderProduct {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="order_product_id")
    private int orderProductId;

    @Column(name="order_product_quantity")
    private int orderProductQuantity;

    @OneToOne(cascade=CascadeType.MERGE)
    @JoinColumn(name="product_id")
    private Product product;

    public boolean removeStockFromProduct() {
        return product.removeProductStock(orderProductQuantity);
    }

    public boolean returnStockFromProduct() {
        return product.returnStock(orderProductQuantity);
    }
}
