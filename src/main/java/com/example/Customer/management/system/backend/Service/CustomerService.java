package com.example.Customer.management.system.backend.Service;

import com.example.Customer.management.system.backend.DTO.*;
import com.example.Customer.management.system.backend.Entity.*;
import com.example.Customer.management.system.backend.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;

    // Create a new customer and map their addresses and mobile numbers from DTO
    public CustomerDTO createCustomer(CustomerDTO dto) {
        Customer customer = CustomerMapper.toEntity(dto);

        // Map addresses and resolve master data (City/Country)
        List<Address> addresses = dto.getAddresses().stream().map(addrDto -> {
            Address address = new Address();
            address.setAddressLine1(addrDto.getAddressLine1());
            address.setAddressLine2(addrDto.getAddressLine2());
            address.setCity(cityRepository.findByName(addrDto.getCity()).orElse(null));
            address.setCountry(countryRepository.findByName(addrDto.getCountry()).orElse(null));
            address.setCustomer(customer);
            return address;
        }).collect(Collectors.toList());

        customer.setAddresses(addresses);
        return CustomerMapper.toDTO(customerRepository.save(customer));
    }

    // Retrieve a customer by ID with all relationships pre-fetched
    public CustomerDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        return CustomerMapper.toDTO(customer);
    }

    // Return all customers
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(CustomerMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Bridge method to handle file upload before passing to batch logic
    public void processBulkUpload(MultipartFile file) {
        // Implement your Excel parsing logic here and return a List<CustomerDTO>
        List<CustomerDTO> dtos = new ArrayList<>();
        bulkCreateCustomers(dtos);
    }

    // Bulk process records using batching to stay within memory limits
    public void bulkCreateCustomers(List<CustomerDTO> dtos) {
        int batchSize = 1000; // Batch limit for performance
        for (int i = 0; i < dtos.size(); i += batchSize) {
            List<Customer> batch = dtos.subList(i, Math.min(i + batchSize, dtos.size()))
                    .stream()
                    .map(CustomerMapper::toEntity)
                    .collect(Collectors.toList());
            customerRepository.saveAll(batch); // Efficient batch database operation
        }
    }

    // Update an existing customer
    public CustomerDTO updateCustomer(Long id, CustomerDTO dto) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Update fields from DTO
        customer.setName(dto.getName());
        customer.setNic(dto.getNic());
        customer.setDob(dto.getDob());

        return CustomerMapper.toDTO(customerRepository.save(customer));
    }
}