package com.example.loantraking.facade;

import com.example.loantraking.Entity.Customer;
import com.example.loantraking.enums.CustomerStatus;
import com.example.loantraking.dto.CustomerDTO;
import com.example.loantraking.exception.DuplicateResourceException;
import com.example.loantraking.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CustomerFacadeImpl implements CustomerFacade {

    private final CustomerService customerService;

    @Override
    public CustomerDTO registerCustomer(CustomerDTO customerDTO) {
        if (customerService.existsByEmail(customerDTO.getEmail())) {
            throw new DuplicateResourceException("Customer with email " + customerDTO.getEmail() + " already exists");
        }
        if (customerService.existsByNationalId(customerDTO.getNationalId())) {
            throw new DuplicateResourceException("Customer with National ID " + customerDTO.getNationalId() + " already exists");
        }
        Customer customer = convertToEntity(customerDTO);
        Customer savedCustomer = customerService.saveCustomer(customer);
        return convertToDTO(savedCustomer);
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerService.getAllCustomers().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<CustomerDTO> getAllCustomers(Pageable pageable) {
        return customerService.getAllCustomers(pageable)
                .map(this::convertToDTO);
    }

    @Override
    public CustomerDTO getCustomer(String id) {
        Customer customer = customerService.getCustomerById(id);
        return convertToDTO(customer);
    }

    @Override
    public Page<CustomerDTO> searchCustomersByEmail(String email, Pageable pageable) {
        return customerService.searchCustomersByEmail(email, pageable)
                .map(this::convertToDTO);
    }

    @Override
    public void updateCustomerImage(String id, byte[] image) {
        customerService.updateCustomerImage(id, image);
    }

    @Override
    public byte[] getCustomerImage(String id) {
        return customerService.getCustomerImage(id);
    }

    @Override
    public void deleteCustomerImage(String id) {
        customerService.deleteCustomerImage(id);
    }

    private Customer convertToEntity(CustomerDTO dto) {
        return Customer.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .address(dto.getAddress())
                .nationalId(dto.getNationalId())
                .status(dto.getStatus() != null ? CustomerStatus.valueOf(dto.getStatus()) : CustomerStatus.PROSPECT)
                .build();
    }

    private CustomerDTO convertToDTO(Customer entity) {
        return CustomerDTO.builder()
                .customerId(entity.getCustomerId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .phoneNumber(entity.getPhoneNumber())
                .address(entity.getAddress())
                .nationalId(entity.getNationalId())
                .status(entity.getStatus() != null ? entity.getStatus().name() : null)
                .createdBy(entity.getCreatedBy())
                .updatedBy(entity.getUpdatedBy())
                .createdDate(entity.getCreatedDate())
                .updatedDate(entity.getUpdatedDate())
                .build();
    }
}
