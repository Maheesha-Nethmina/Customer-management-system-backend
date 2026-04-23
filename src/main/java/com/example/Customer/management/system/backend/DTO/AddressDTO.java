package com.example.Customer.management.system.backend.DTO;

import lombok.Data;

@Data
public class AddressDTO {
    private String addressLine1; // First line of address
    private String addressLine2; // Second line of address
    private String city; // City name
    private String country; // Country name
}