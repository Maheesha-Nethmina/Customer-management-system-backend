package com.example.Customer.management.system.backend.DTO;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class CustomerDTO {
    private String name; // Mandatory customer name
    private LocalDate dob; // Customer date of birth
    private String nic; // Unique NIC number
    private List<MobileNumberDTO> mobileNumbers; // List of optional numbers
    private List<AddressDTO> addresses; // List of optional addresses
    private List<Long> familyMemberIds; // Only pass IDs for family members
}