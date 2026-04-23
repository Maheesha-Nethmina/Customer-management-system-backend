package com.example.Customer.management.system.backend.DTO;

import com.example.Customer.management.system.backend.Entity.*;
import java.util.stream.Collectors;

public class CustomerMapper {

    // Entity mapping (We keep this simple because the Service layer handles the complex list saving)
    public static Customer toEntity(CustomerDTO dto) {
        Customer customer = new Customer();
        customer.setName(dto.getName());
        customer.setDob(dto.getDob());
        customer.setNic(dto.getNic());
        return customer;
    }

    // DTO mapping (This MUST map everything so the React frontend can display it)
    public static CustomerDTO toDTO(Customer entity) {
        CustomerDTO dto = new CustomerDTO();

        // FIXED: Changed 'customer' to 'entity'
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDob(entity.getDob());
        dto.setNic(entity.getNic());

        // Map Mobile Numbers safely
        if (entity.getMobileNumbers() != null) {
            dto.setMobileNumbers(entity.getMobileNumbers().stream().map(mob -> {
                MobileNumberDTO mobDto = new MobileNumberDTO();
                mobDto.setNumber(mob.getNumber());
                return mobDto;
            }).collect(Collectors.toList()));
        }

        // Map Addresses safely
        if (entity.getAddresses() != null) {
            dto.setAddresses(entity.getAddresses().stream().map(addr -> {
                AddressDTO addrDto = new AddressDTO();
                addrDto.setAddressLine1(addr.getAddressLine1());
                addrDto.setAddressLine2(addr.getAddressLine2());
                if (addr.getCity() != null) addrDto.setCity(addr.getCity().getName());
                if (addr.getCountry() != null) addrDto.setCountry(addr.getCountry().getName());
                return addrDto;
            }).collect(Collectors.toList()));
        }

        // Map Family Members safely
        if (entity.getFamilyMembers() != null) {
            // 1. Map just the IDs (for the edit form dropdowns)
            dto.setFamilyMemberIds(entity.getFamilyMembers().stream()
                    .map(Customer::getId)
                    .collect(Collectors.toList()));

            // 2. Map the basic details (for the view profile table)
            // We only map basic fields here to prevent an infinite loop!
            dto.setFamilyMembers(entity.getFamilyMembers().stream().map(fm -> {
                CustomerDTO fmDto = new CustomerDTO();
                fmDto.setId(fm.getId());
                fmDto.setName(fm.getName());
                fmDto.setNic(fm.getNic());
                return fmDto;
            }).collect(Collectors.toList()));
        }

        return dto;
    }
}