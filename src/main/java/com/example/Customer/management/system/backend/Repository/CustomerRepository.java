package com.example.Customer.management.system.backend.Repository;

import com.example.Customer.management.system.backend.Entity.Customer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // Using EntityGraph is cleaner than JPQL JOIN FETCH.
    // It tells Hibernate to fetch these associations eagerly in one SQL query.
    @EntityGraph(attributePaths = {"mobileNumbers", "addresses", "familyMembers"})
    Optional<Customer> findById(Long id);

    // Add a lookup by NIC, as it's a unique field and essential for bulk updates
    Optional<Customer> findByNic(String nic);
}