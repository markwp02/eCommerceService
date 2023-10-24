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
    public List<Product> findAll(@RequestParam(required = false) String productName) {
        List<Product> products;

        if(productName != null) {
            products = productService.findByProductNameContaining(productName);
        } else {
            products = productService.findAll();
        }
        return products;
    }

    @GetMapping("/productItems/{theId}")
    public Product findById(@PathVariable int theId) {
        return productService.findById(theId);
    }

    @GetMapping("/productItems/category/{theCategory}")
    public List<Product> findByCategory(@PathVariable String theCategory) {
        return productService.findByProductCategory(theCategory);
    }

    @GetMapping("/productItems/category")
    public List<String> findProductCategories() {
        return productService.findProductCategories();
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

    @DeleteMapping("/productItems/{theId}")
    public String deleteProduct(@PathVariable int theId) {
        productService.deleteById(theId);

        return "Deleted product id - " + theId;
    }
}
