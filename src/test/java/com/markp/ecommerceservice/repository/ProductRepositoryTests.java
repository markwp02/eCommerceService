package com.markp.ecommerceservice.repository;

import com.markp.ecommerceservice.entity.Product;
import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * JPA tests that use an embedded in memory H2 database instead of the one
 * configured in config
 */
@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductRepositoryTests {

    @Autowired
    ProductRepository productRepository;

    List<Product> productList;

    private final String productCategory1 = "Videogames";
    private final String productCategory2 = "Laptops";

    private final int targetProductId = 1;

    private final String productName1 = "Microsoft Xbox Series X";
    private final BigDecimal productPrice1 = BigDecimal.valueOf(749.99);
    private final String productDescription1 = "Microsoft videogame console";
    private final int productStock1 = 40;

    private final String productName2 = "Sony Playstation 5";
    private final BigDecimal productPrice2 = BigDecimal.valueOf(810.00);
    private final String productDescription2 = "Sony videogames console";
    private final int productStock2 = 9;

    private final String productName3 = "Microsoft Surface Laptop";
    private final BigDecimal productPrice3 = BigDecimal.valueOf(1260.70);
    private final String productDescription3 = "Microsoft Laptop computer";
    private final int productStock3 = 13;

    @BeforeAll
    public void setup() {

        Product product1 = Product.builder()
                .productName(productName1)
                .productPrice(productPrice1)
                .productCategory(productCategory1)
                .productDescription(productDescription1)
                .productStock(productStock1)
                .build();

        Product product2 = Product.builder()
                .productName(productName2)
                .productPrice(productPrice2)
                .productCategory(productCategory1)
                .productDescription(productDescription2)
                .productStock(productStock2)
                .build();

        Product product3 = Product.builder()
                .productName(productName3)
                .productPrice(productPrice3)
                .productCategory(productCategory2)
                .productDescription(productDescription3)
                .productStock(productStock3)
                .build();

        productList = new ArrayList<>(Arrays.asList(product1, product2, product3));
    }

    @Test
    @Order(1)
    @Rollback(value = false)
    public void saveProductTest() {

        productList.forEach(product -> {
            productRepository.save(product);
            Assertions.assertThat(product.getProductId()).isGreaterThan(0);
        });
    }

    @Test
    @Order(2)
    public void getRestaurantTest() {

        Product product = productRepository.findById(targetProductId).get();
        Assertions.assertThat(product.getProductId()).isEqualTo(targetProductId);
    }

    @Test
    @Order(3)
    public void getListOfProductsTest() {

        List<Product> databaseProducts = productRepository.findAll();
        Assertions.assertThat(databaseProducts.size()).isEqualTo(productList.size());
    }

    @Test
    @Order(4)
    public void getListOfProductsCategoryTest() {

        List<Product> databaseProducts = productRepository.findByProductCategory(productCategory1);
        List<Product> videoGamesList = productList
                .stream()
                .filter(product -> product.getProductCategory().equals(productCategory1))
                .collect(Collectors.toList());

        Assertions.assertThat(databaseProducts.size()).isEqualTo(videoGamesList.size());
    }

    @Test
    @Order(5)
    public void getListOfProductsSearch() {

        String searchTerm = "microsoft";

        List<Product> databaseProducts = productRepository.findByProductNameContainingIgnoreCase(searchTerm);
        List<Product> searchTermList = productList
                .stream()
                .filter(product -> product.getProductName().toLowerCase(Locale.ROOT).contains(searchTerm))
                .collect(Collectors.toList());

        Assertions.assertThat(databaseProducts.size()).isEqualTo(searchTermList.size());
    }

    @Test
    @Order(6)
    @Rollback(value = false)
    public void updateProductTest() {

        String updatedProductName = "Xbox Series S";
        BigDecimal updatedProductPrice = BigDecimal.valueOf(549.00);

        Product product = productRepository.findById(targetProductId).get();
        product.setProductName(updatedProductName);
        product.setProductPrice(updatedProductPrice);

        Product updatedProduct = productRepository.save(product);
        Assertions.assertThat(updatedProduct.getProductName()).isEqualTo(updatedProductName);
        Assertions.assertThat(updatedProduct.getProductPrice()).isEqualTo(updatedProductPrice);
    }

    @Test
    @Order(7)
    @Rollback(value = false)
    public void deleteProductTest() {

        productRepository.deleteById(targetProductId);

        Optional<Product> optionalProduct = productRepository.findById(targetProductId);

        Product product = null;
        if(optionalProduct.isPresent()) {
            product = optionalProduct.get();
        }

        Assertions.assertThat(product).isNull();
    }
}
