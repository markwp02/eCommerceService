package com.markp.ecommerceservice.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.markp.ecommerceservice.entity.CustomerOrder;
import com.markp.ecommerceservice.entity.OrderProduct;
import com.markp.ecommerceservice.entity.OrderStatus;
import com.markp.ecommerceservice.entity.Product;
import com.markp.ecommerceservice.repository.CustomerOrderRepository;
import com.markp.ecommerceservice.repository.ProductRepository;
import com.markp.ecommerceservice.service.OrderStatusService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    OrderStatusService orderStatusService;

    @Autowired
    private ObjectMapper objectMapper;

    private int CONFIRMED_STATUS = 1;
    private int DISPATCHED_STATUS = 2;
    private int REJECTED_STATUS = 5;

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

        OrderProduct orderProduct = OrderProduct.builder()
                .orderProductQuantity(5)
                .product(product)
                .build();

        CustomerOrder newCustomerOrder = CustomerOrder.builder()
                .customerOrderTotalPrice(BigDecimal.valueOf(35))
                .orderProducts(Arrays.asList(orderProduct))
                .build();
        return newCustomerOrder;
    }

    @BeforeEach
    public void setup() {
        customerOrderRepository.deleteAll();
    }

    @Test
    public void createCustomerOrderAndUpdateProductStock() throws Exception {

        // given
        Product product = createAndSaveProductToDatabase();
        CustomerOrder customerOrder = createCustomerOrderWithOrderProduct(product);

        // when
        ResultActions response = mockMvc.perform(post("/api/customerOrders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerOrder)));

        // then
        OrderProduct orderProduct = customerOrder.getOrderProducts().get(0);
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
        Product product = createAndSaveProductWithLimitedStockToDatabase();
        CustomerOrder customerOrder = createCustomerOrderWithOrderProduct(product);

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
    public void getAllSavedCustomerOrders() throws Exception {

        // given
        Product product = createAndSaveProductWithLimitedStockToDatabase();
        CustomerOrder customerOrder1 = createCustomerOrderWithOrderProduct(product);
        CustomerOrder customerOrder2 = createCustomerOrderWithOrderProduct(product);
        customerOrderRepository.save(customerOrder1);
        customerOrderRepository.save(customerOrder2);

        // when
        ResultActions response = mockMvc.perform(get("/api/customerOrders"));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$[0].customerOrderId").exists())
                .andExpect(jsonPath("$[1].customerOrderId").exists())
                .andExpect(jsonPath("$[2].customerOrderId").doesNotExist())
                .andReturn();

    }

    @Test
    public void getAllCustomerOrdersWithoutAnySaved() throws Exception {

        // when
        ResultActions response = mockMvc.perform(get("/api/customerOrders"));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$[0]").doesNotExist())
                .andReturn();

    }

    @Test
    public void getCustomerOrderWithId() throws Exception {

        // given
        Product product = createAndSaveProductToDatabase();
        CustomerOrder customerOrder = createCustomerOrderWithOrderProduct(product);
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

    @Test
    public void updateCustomerOrderStatus() throws Exception {

        // given
        Product product = createAndSaveProductToDatabase();
        CustomerOrder customerOrder = createCustomerOrderWithOrderProduct(product);
        customerOrder = customerOrderRepository.save(customerOrder);

        CustomerOrder updatedCustomerOrder = createCustomerOrderWithOrderProduct(product);
        updatedCustomerOrder.setCustomerOrderId(customerOrder.getCustomerOrderId());

        OrderStatus dispatchedOrderStatus = orderStatusService.findById(DISPATCHED_STATUS);
        updatedCustomerOrder.setOrderStatus(dispatchedOrderStatus);

        // when
        ResultActions response = mockMvc.perform(put("/api/customerOrders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedCustomerOrder)));;

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerOrderId", is(customerOrder.getCustomerOrderId())))
                .andExpect(jsonPath("$.orderStatus.orderStatusId", is(DISPATCHED_STATUS)))
                .andReturn();
    }

    @Test
    public void deleteCustomerOrder() throws Exception {
        // given
        Product product = createAndSaveProductToDatabase();
        CustomerOrder customerOrder = createCustomerOrderWithOrderProduct(product);
        customerOrder = customerOrderRepository.save(customerOrder);
        int customerOrderId = customerOrder.getCustomerOrderId();

        // when
        ResultActions response = mockMvc.perform(delete("/api/customerOrders/{id}", customerOrderId));

        // then
        Exception exception = assertThrows(ServletException.class, () -> {
            mockMvc.perform(get("/api/customerOrders/{id}", customerOrderId));
        });

        String expectedMessage = "Did not find customer order with id - " + customerOrderId;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
