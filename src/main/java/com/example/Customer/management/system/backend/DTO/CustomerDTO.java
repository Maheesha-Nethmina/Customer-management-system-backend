package com.example.Customer.management.system.backend.DTO;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class CustomerDTO {
    private Long id;
    private String name;
    private LocalDate dob;
    private String nic;

    private List<MobileNumberDTO> mobileNumbers;
    private List<AddressDTO> addresses;

    private List<Long> familyMemberIds;
    private List<CustomerDTO> familyMembers;
}