package com.markp.ecommerceservice.dto;

import lombok.Data;

@Data
public class LoginResponseDTO {

    private int customerId;
    private String customerUsername;
    private String customerFirstName;
    private String customerLastName;
    private String customerEmail;

    String errorMessage;

    public LoginResponseDTO(int customerId, String customerUsername, String customerFirstName, String customerLastName, String customerEmail) {
        this.customerId = customerId;
        this.customerUsername = customerUsername;
        this.customerFirstName = customerFirstName;
        this.customerLastName = customerLastName;
        this.customerEmail = customerEmail;
    }

    public LoginResponseDTO(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
