package com.example.loantraking.service;

import com.example.loantraking.Entity.Customer;
import com.example.loantraking.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public Customer saveCustomer(Customer customer) {
        log.info("Saving customer: {}", customer);
        return customerRepository.save(customer);
    }

    @Override
    public List<Customer> getAllCustomers() {
        log.info("Fetching all customers");
        return customerRepository.findAll();
    }

    @Override
    public Page<Customer> getAllCustomers(Pageable pageable) {
        log.info("Fetching all customers with pagination: {}", pageable);
        return customerRepository.findAll(pageable);
    }

    @Override
    public Customer getCustomerById(String id) {
        log.info("Fetching customer by id: {}", id);
        return customerRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Customer not found with id: {}", id);
                    return new RuntimeException("Customer not found with id: " + id);
                });
    }

    @Override
    public boolean existsByEmail(String email) {
        return customerRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByNationalId(String nationalId) {
        return customerRepository.existsByNationalId(nationalId);
    }

    @Override
    public Page<Customer> searchCustomersByEmail(String email, Pageable pageable) {
        log.info("Searching customers by email: {}", email);
        return customerRepository.findByEmailContaining(email, pageable);
    }

    @Override
    public void updateCustomerImage(String id, byte[] image) {
        log.info("Updating image for customer with id: {}", id);
        Customer customer = getCustomerById(id);
        customer.setCustomerImage(image);
        customerRepository.save(customer);
    }

    @Override
    public byte[] getCustomerImage(String id) {
        log.info("Fetching image for customer with id: {}", id);
        Customer customer = getCustomerById(id);
        return customer.getCustomerImage();
    }

    @Override
    public void deleteCustomerImage(String id) {
        log.info("Deleting image for customer with id: {}", id);
        Customer customer = getCustomerById(id);
        customer.setCustomerImage(null);
        customerRepository.save(customer);
    }
}
