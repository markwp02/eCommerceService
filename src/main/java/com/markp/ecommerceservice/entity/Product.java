package com.markp.ecommerceservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="product")
public class Product {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="product_id")
    private int productId;

    @Column(name="product_name")
    private String productName;

    @Column(name="product_category")
    private String productCategory;

    @Column(name="product_description")
    private String productDescription;

    @Column(name="product_price")
    private BigDecimal productPrice;

    @Column(name="product_stock")
    private int productStock;

    @Column(name="product_image_url")
    private String productImageUrl;

    public boolean removeProductStock(int stockToRemove) {
        int newStock = productStock - stockToRemove;
        boolean result;

        if(newStock >= 0 && stockToRemove > 0) {
            productStock = newStock;
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    public boolean returnStock(int stockToReturn) {
        int newStock = productStock + stockToReturn;
        boolean result;

        if(stockToReturn > 0) {
            productStock = newStock;
            result = true;
        } else {
            result = false;
        }
        return result;
    }
}
