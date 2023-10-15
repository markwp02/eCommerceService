package com.markp.ecommerceservice.rest;

import com.markp.ecommerceservice.entity.Product;

import com.markp.ecommerceservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductRestController {

    @Autowired
    private ProductService productService;

    @GetMapping("/productItems")
    public List<Product> findAll() {
        return productService.findAll();
    }

    @GetMapping("/productItems/{productId}")
    public Product findById(@PathVariable int theId) {
        Product product = productService.findById(theId);
        return product;
    }

    @PostMapping("/productItems")
    public Product addProduct(@RequestBody Product theProduct) {
        productService.add(theProduct);
        return theProduct;
    }

    @PutMapping("/productItems")
    public Product updateProduct(@RequestBody Product theProduct) {
        productService.update(theProduct);
        return theProduct;
    }

    @DeleteMapping("/productItems/{productId}")
    public String deleteProduct(@PathVariable int theId) {
        productService.deleteById(theId);

        return "Deleted product id - " + theId;
    }
}
