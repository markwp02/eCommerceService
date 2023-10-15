package com.markp.ecommerceservice.service;

import com.markp.ecommerceservice.entity.Product;
import com.markp.ecommerceservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product findById(int theId) {
        Optional<Product> result = productRepository.findById(theId);

        Product product;
        if(result.isPresent()) {
            product = result.get();
        } else {
            throw new RuntimeException("Did not find product with id - " + theId);
        }
        return product;
    }

    /**
     * Separate methods to add a new product. This will force the save
     * of a new item in case a productId is passed in the Product object.
     * @param theProduct
     */
    @Override
    public void add(Product theProduct) {
        theProduct.setProductId(0);
        productRepository.save(theProduct);
    }

    @Override
    public void update(Product theProduct) {
        productRepository.save(theProduct);
    }

    @Override
    public void deleteById(int theId) {
        Optional<Product> result = productRepository.findById(theId);

        if(!result.isPresent()) {
            throw new RuntimeException("Product not found - " + theId);
        } else {
            productRepository.deleteById(theId);
        }
    }
}
