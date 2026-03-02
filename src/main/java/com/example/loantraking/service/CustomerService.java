package com.example.loantraking.service;

import com.example.loantraking.Entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerService {
    Customer saveCustomer(Customer customer);
    List<Customer> getAllCustomers();
    Page<Customer> getAllCustomers(Pageable pageable);
    Customer getCustomerById(String id);
    boolean existsByEmail(String email);
    boolean existsByNationalId(String nationalId);
    Page<Customer> searchCustomersByEmail(String email, Pageable pageable);
    void updateCustomerImage(String id, byte[] image);
    byte[] getCustomerImage(String id);
    void deleteCustomerImage(String id);
}
