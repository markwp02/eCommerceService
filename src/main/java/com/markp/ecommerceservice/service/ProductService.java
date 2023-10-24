package com.markp.ecommerceservice.service;

import com.markp.ecommerceservice.entity.Product;

import java.util.List;

public interface ProductService {

    public List<Product> findAll();

    public Product findById(int theId);

    public List<Product> findByProductCategory(String productCategory);

    public List<Product> findByProductNameContaining(String productName);

    public List<String> findProductCategories();

    public void add(Product theProduct);

    public void update(Product theProduct);

    public void deleteById(int theId);
}
