package com.example.Customer.management.system.backend.Service;

import com.example.Customer.management.system.backend.DTO.*;
import com.example.Customer.management.system.backend.Entity.*;
import com.example.Customer.management.system.backend.Repository.*;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
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

    // Create a new customer
    public CustomerDTO createCustomer(CustomerDTO dto) {
        try {
            Customer customer = CustomerMapper.toEntity(dto);

            // Map addresses and resolve master data (City and Country)
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
        } catch (Exception e) {
            throw new RuntimeException("Failed to create customer: " + e.getMessage());
        }
    }

    // get a customer by ID
    public CustomerDTO getCustomerById(Long id) {
        try {
            Customer customer = customerRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Customer ID " + id + " not found."));
            return CustomerMapper.toDTO(customer);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    // get all customers
    public List<CustomerDTO> getAllCustomers() {
        try {
            return customerRepository.findAll().stream()
                    .map(CustomerMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve customers: " + e.getMessage());
        }
    }

    // method to handle file upload
    public void processBulkUpload(MultipartFile file) {
        try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            List<CustomerDTO> batch = new ArrayList<>();

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row

                try {
                    CustomerDTO dto = new CustomerDTO();
                    dto.setName(row.getCell(0).getStringCellValue()); // Map Name from Column 0
                    dto.setDob(LocalDate.parse(row.getCell(1).getStringCellValue())); // Map DOB from Column 1
                    dto.setNic(row.getCell(2).getStringCellValue()); // Map NIC from Column 2

                    batch.add(dto);
                } catch (Exception rowEx) {
                    // Log the error for this specific row, but don't crash the whole upload
                    System.err.println("Skipping invalid row " + row.getRowNum() + ": " + rowEx.getMessage());
                }

                // Trigger batch processing every 1000 records
                if (batch.size() >= 1000) {
                    bulkCreateCustomers(new ArrayList<>(batch));
                    batch.clear();
                }
            }
            if (!batch.isEmpty()) bulkCreateCustomers(batch); // Save remaining records
        } catch (Exception e) {
            throw new RuntimeException("Failed to process Excel file: " + e.getMessage());
        }
    }

    // Bulk process records using batching to stay within memory limits
    public void bulkCreateCustomers(List<CustomerDTO> dtos) {
        try {
            int batchSize = 1000; // Batch limit for performance
            for (int i = 0; i < dtos.size(); i += batchSize) {
                List<Customer> batch = dtos.subList(i, Math.min(i + batchSize, dtos.size()))
                        .stream()
                        .map(CustomerMapper::toEntity)
                        .collect(Collectors.toList());
                customerRepository.saveAll(batch); // Efficient batch database operation
            }
        } catch (Exception e) {
            throw new RuntimeException("Database error during bulk save: " + e.getMessage());
        }
    }

    // Update an existing customer
    public CustomerDTO updateCustomer(Long id, CustomerDTO dto) {
        try {
            Customer customer = customerRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Cannot update. Customer ID " + id + " not found."));

            // Update fields from DTO
            customer.setName(dto.getName());
            customer.setNic(dto.getNic());
            customer.setDob(dto.getDob());

            return CustomerMapper.toDTO(customerRepository.save(customer));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}