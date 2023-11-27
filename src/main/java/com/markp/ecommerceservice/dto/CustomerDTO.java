package com.markp.ecommerceservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {

    private int customerId;

    private String customerUsername;

    private String customerPassword;

    private String customerFirstName;

    private String customerLastName;

    private String customerEmail;

}
