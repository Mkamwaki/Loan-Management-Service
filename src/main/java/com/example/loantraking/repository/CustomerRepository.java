package com.example.loantraking.repository;

import com.example.loantraking.Entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    boolean existsByEmail(String email);
    boolean existsByNationalId(String nationalId);
    Page<Customer> findByEmailContaining(String email, Pageable pageable);
}
