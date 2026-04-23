package com.example.Customer.management.system.backend.DTO;

import com.example.Customer.management.system.backend.Entity.*;
import java.util.stream.Collectors;

public class CustomerMapper {

    public static Customer toEntity(CustomerDTO dto) {
        Customer customer = new Customer();
        customer.setName(dto.getName()); // Set customer name
        customer.setDob(dto.getDob()); // Set date of birth
        customer.setNic(dto.getNic()); // Set NIC number
        return customer;
    }

    public static CustomerDTO toDTO(Customer entity) {
        CustomerDTO dto = new CustomerDTO();
        dto.setName(entity.getName()); // Map name from entity
        dto.setDob(entity.getDob()); // Map DOB from entity
        dto.setNic(entity.getNic()); // Map NIC from entity
        return dto;
    }
}