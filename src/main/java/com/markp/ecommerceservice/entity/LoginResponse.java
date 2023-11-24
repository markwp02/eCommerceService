package com.markp.ecommerceservice.entity;

import lombok.Data;

@Data
public class LoginResponse {

    private int customerId;
    private String customerUsername;
    private String customerFirstName;
    private String customerLastName;
    private String customerEmail;

    String errorMessage;

    public LoginResponse(int customerId, String customerUsername, String customerFirstName, String customerLastName, String customerEmail) {
        this.customerId = customerId;
        this.customerUsername = customerUsername;
        this.customerFirstName = customerFirstName;
        this.customerLastName = customerLastName;
        this.customerEmail = customerEmail;
    }

    public LoginResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
