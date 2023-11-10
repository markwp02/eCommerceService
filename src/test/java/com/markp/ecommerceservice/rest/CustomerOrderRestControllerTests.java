package com.markp.ecommerceservice.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.markp.ecommerceservice.entity.CustomerOrder;
import com.markp.ecommerceservice.entity.OrderProduct;
import com.markp.ecommerceservice.entity.Product;
import com.markp.ecommerceservice.repository.CustomerOrderRepository;
import com.markp.ecommerceservice.repository.ProductRepository;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.CoreMatchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CustomerOrderRestControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CustomerOrderRepository customerOrderRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private int CONFIRMED_STATUS = 1;
    private int REJECTED_STATUS = 5;

    private Product product;
    private OrderProduct orderProduct;
    private CustomerOrder customerOrder;

    private Product createAndSaveProductToDatabase() {

        Product newProduct = Product.builder()
                .productName("Blue pen")
                .productDescription("Blue pen")
                .productCategory("Stationary")
                .productPrice(BigDecimal.valueOf(3.5))
                .productStock(10)
                .build();

        productRepository.save(newProduct);

        return newProduct;
    }

    private Product createAndSaveProductWithLimitedStockToDatabase() {

        Product newProduct = Product.builder()
                .productName("Gold pen")
                .productDescription("Gold pen")
                .productCategory("Stationary")
                .productPrice(BigDecimal.valueOf(3005.99))
                .productStock(1)
                .build();

        productRepository.save(newProduct);

        return newProduct;
    }

    private CustomerOrder createCustomerOrderWithOrderProduct(Product product) {

        orderProduct = OrderProduct.builder()
                .orderProductQuantity(5)
                .product(product)
                .build();

        customerOrder = CustomerOrder.builder()
                .customerOrderTotalPrice(BigDecimal.valueOf(35))
                .orderProducts(Arrays.asList(orderProduct))
                .build();
        return customerOrder;
    }

    @Test
    public void createCustomerOrderAndUpdateProductStock() throws Exception {

        // given
        product = createAndSaveProductToDatabase();
        customerOrder = createCustomerOrderWithOrderProduct(product);

        // when
        ResultActions response = mockMvc.perform(post("/api/customerOrders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerOrder)));

        // then
        int expectedStock = product.getProductStock() - orderProduct.getOrderProductQuantity();

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerOrderId").isNumber())
                .andExpect(jsonPath("$.orderProducts[0].product.productStock", is(expectedStock)))
                .andExpect(jsonPath("$.orderStatus.orderStatusId", is(CONFIRMED_STATUS)))
                .andReturn();
    }

    @Test
    public void createCustomerOrderAndLeaveStockUnchanged() throws Exception {

        // given
        product = createAndSaveProductWithLimitedStockToDatabase();
        customerOrder = createCustomerOrderWithOrderProduct(product);

        // when
        ResultActions response = mockMvc.perform(post("/api/customerOrders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerOrder)));

        // then
        int expectedStock = product.getProductStock();

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.customerOrderId").isNumber())
                .andExpect(jsonPath("$.orderProducts[0].product.productStock", is(expectedStock)))
                .andExpect(jsonPath("$.orderStatus.orderStatusId", is(REJECTED_STATUS)))
                .andReturn();
    }

    @Test
    public void getCustomerOrderWithId() throws Exception {

        // given
        product = createAndSaveProductToDatabase();
        customerOrder = createCustomerOrderWithOrderProduct(product);
        customerOrder = customerOrderRepository.save(customerOrder);

        int customerOrderId = customerOrder.getCustomerOrderId();
        // when
        ResultActions response = mockMvc.perform(get("/api/customerOrders/{id}", customerOrderId));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.customerOrderId", is(customerOrderId)))
                .andExpect(jsonPath("$.customerOrderTotalPrice", is(customerOrder.getCustomerOrderTotalPrice().doubleValue())))
                .andReturn();
    }

    @Test
    public void getCustomerOrderWithInvalidId() {

        //given
        int customerOrderId = 0;

        // when
        Exception exception = assertThrows(ServletException.class, () -> {
            mockMvc.perform(get("/api/customerOrders/{id}", customerOrderId));
        });

        // then
        String expectedMessage = "Did not find customer order with id - " + customerOrderId;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
