package com.markp.ecommerceservice.repository;

import com.markp.ecommerceservice.entity.Product;
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

    private final String PRODUCT_CATEGORY_1 = "Videogames";
    private final String PRODUCT_CATEGORY_2 = "Laptops";

    private final int TARGET_PRODUCT_ID = 1;

    private final String PRODUCT_NAME_1 = "Microsoft Xbox Series X";
    private final BigDecimal PRODUCT_PRICE_1 = BigDecimal.valueOf(749.99);
    private final String PRODUCT_DESCRIPTION_1 = "Microsoft videogame console";
    private final int PRODUCT_STOCK_1 = 40;

    private final String PRODUCT_NAME_2 = "Sony Playstation 5";
    private final BigDecimal PRODUCT_PRICE_2 = BigDecimal.valueOf(810.00);
    private final String PRODUCT_DESCRIPTION_2 = "Sony videogames console";
    private final int PRODUCT_STOCK_2 = 9;

    private final String PRODUCT_NAME_3 = "Microsoft Surface Laptop";
    private final BigDecimal PRODUCT_PRICE_3 = BigDecimal.valueOf(1260.70);
    private final String PRODUCT_DESCRIPTION_3 = "Microsoft Laptop computer";
    private final int PRODUCT_STOCK_3 = 13;

    @BeforeAll
    public void setup() {

        Product product1 = Product.builder()
                .productName(PRODUCT_NAME_1)
                .productPrice(PRODUCT_PRICE_1)
                .productCategory(PRODUCT_CATEGORY_1)
                .productDescription(PRODUCT_DESCRIPTION_1)
                .productStock(PRODUCT_STOCK_1)
                .build();

        Product product2 = Product.builder()
                .productName(PRODUCT_NAME_2)
                .productPrice(PRODUCT_PRICE_2)
                .productCategory(PRODUCT_CATEGORY_1)
                .productDescription(PRODUCT_DESCRIPTION_2)
                .productStock(PRODUCT_STOCK_2)
                .build();

        Product product3 = Product.builder()
                .productName(PRODUCT_NAME_3)
                .productPrice(PRODUCT_PRICE_3)
                .productCategory(PRODUCT_CATEGORY_2)
                .productDescription(PRODUCT_DESCRIPTION_3)
                .productStock(PRODUCT_STOCK_3)
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

        Product product = productRepository.findById(TARGET_PRODUCT_ID).get();
        Assertions.assertThat(product.getProductId()).isEqualTo(TARGET_PRODUCT_ID);
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

        List<Product> databaseProducts = productRepository.findByProductCategory(PRODUCT_CATEGORY_1);
        List<Product> videoGamesList = productList
                .stream()
                .filter(product -> product.getProductCategory().equals(PRODUCT_CATEGORY_1))
                .collect(Collectors.toList());

        Assertions.assertThat(databaseProducts.size()).isEqualTo(videoGamesList.size());
    }

    @Test
    @Order(5)
    public void getListOfCategoriesTest() {

        List<String> databaseCategories = productRepository.findProductCategories();

        List<String> productCategories = productList
                .stream()
                .map(Product::getProductCategory)
                .distinct()
                .collect(Collectors.toList());

        Assertions.assertThat(databaseCategories).containsAll(productCategories);
        Assertions.assertThat(productCategories).containsAll(databaseCategories);
    }

    @Test
    @Order(6)
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
    @Order(7)
    @Rollback(value = false)
    public void updateProductTest() {

        String updatedProductName = "Xbox Series S";
        BigDecimal updatedProductPrice = BigDecimal.valueOf(549.00);

        Product product = productRepository.findById(TARGET_PRODUCT_ID).get();
        product.setProductName(updatedProductName);
        product.setProductPrice(updatedProductPrice);

        Product updatedProduct = productRepository.save(product);
        Assertions.assertThat(updatedProduct.getProductName()).isEqualTo(updatedProductName);
        Assertions.assertThat(updatedProduct.getProductPrice()).isEqualTo(updatedProductPrice);
    }

    @Test
    @Order(8)
    @Rollback(value = false)
    public void deleteProductTest() {

        productRepository.deleteById(TARGET_PRODUCT_ID);

        Optional<Product> optionalProduct = productRepository.findById(TARGET_PRODUCT_ID);

        Product product = null;
        if(optionalProduct.isPresent()) {
            product = optionalProduct.get();
        }

        Assertions.assertThat(product).isNull();
    }
}
