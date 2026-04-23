package com.example.Customer.management.system.backend.Controller;

import com.example.Customer.management.system.backend.DTO.CustomerDTO;
import com.example.Customer.management.system.backend.Service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CustomerController {

    private final CustomerService customerService;

    // UPDATED: View all customers with pagination to prevent memory overload
    @GetMapping("/all")
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size
    ) {
        try {
            // Uses the new paginated method from CustomerService
            return ResponseEntity.ok(customerService.getAllCustomersPaginated(page, size));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // View specific customer details
    @GetMapping("/view/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(customerService.getCustomerById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Create a new customer
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CustomerDTO customerDTO) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(customerService.createCustomer(customerDTO));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Update an existing customer
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody CustomerDTO customerDTO) {
        try {
            return ResponseEntity.ok(customerService.updateCustomer(id, customerDTO));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Process bulk upload via Excel file
    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty. Please upload a valid Excel file.");
            }
            customerService.processBulkUpload(file);
            return ResponseEntity.ok("Bulk upload processed successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}