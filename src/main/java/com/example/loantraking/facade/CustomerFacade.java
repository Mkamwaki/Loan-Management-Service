package com.example.loantraking.facade;

import com.example.loantraking.dto.CustomerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerFacade {
    CustomerDTO registerCustomer(CustomerDTO customerDTO);
    List<CustomerDTO> getAllCustomers();
    Page<CustomerDTO> getAllCustomers(Pageable pageable);
    CustomerDTO getCustomer(String id);
    Page<CustomerDTO> searchCustomersByEmail(String email, Pageable pageable);
    void updateCustomerImage(String id, byte[] image);
    byte[] getCustomerImage(String id);
    void deleteCustomerImage(String id);
}
