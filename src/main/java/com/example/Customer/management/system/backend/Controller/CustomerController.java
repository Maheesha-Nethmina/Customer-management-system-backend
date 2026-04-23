package com.example.Customer.management.system.backend.Controller;

import com.example.Customer.management.system.backend.DTO.CustomerDTO;
import com.example.Customer.management.system.backend.Service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor // Automatically injects CustomerService via constructor
@CrossOrigin(origins = "*") // Allows React frontend to access the API
public class CustomerController {

    private final CustomerService customerService;

    // View all customers in table view
    @GetMapping("/all")
    public ResponseEntity<List<CustomerDTO>> getAll() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    // View specific customer details
    @GetMapping("/view/{id}")
    public ResponseEntity<CustomerDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    // Create a new customer
    @PostMapping("/create")
    public ResponseEntity<CustomerDTO> create(@RequestBody CustomerDTO customerDTO) {
        return ResponseEntity.ok(customerService.createCustomer(customerDTO));
    }

    // Update an existing customer
    @PutMapping("/update/{id}")
    public ResponseEntity<CustomerDTO> update(@PathVariable Long id, @RequestBody CustomerDTO customerDTO) {
        return ResponseEntity.ok(customerService.updateCustomer(id, customerDTO));
    }

    // Process bulk upload via Excel file
    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
        customerService.processBulkUpload(file);
        return ResponseEntity.ok("Bulk upload processed successfully");
    }
}