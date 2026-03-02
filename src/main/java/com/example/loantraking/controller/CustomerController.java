package com.example.loantraking.controller;

import com.example.loantraking.dto.CustomerDTO;
import com.example.loantraking.facade.CustomerFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping({"loan/customers", "loan/custormers"})
@RequiredArgsConstructor
@Slf4j
public class CustomerController {

    private final CustomerFacade customerFacade;

    @PostMapping("/register")
    public ResponseEntity<CustomerDTO> registerCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        log.info("Received request to register customer: {}", customerDTO);
        return ResponseEntity.ok(customerFacade.registerCustomer(customerDTO));
    }

    @GetMapping
    public ResponseEntity<Page<CustomerDTO>> getAllCustomers(@PageableDefault(size = 10) Pageable pageable) {
        log.info("Received request to get all customers with pagination: {}", pageable);
        return ResponseEntity.ok(customerFacade.getAllCustomers(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable String id) {
        log.info("Received request to get customer with id: {}", id);
        return ResponseEntity.ok(customerFacade.getCustomer(id));
    }

    @PostMapping("/search")
    public ResponseEntity<Page<CustomerDTO>> searchCustomersByEmail(
            @RequestParam String email,
            @PageableDefault(size = 10) Pageable pageable) {
        log.info("Received request to search customers by email: {} with pagination: {}", email, pageable);
        return ResponseEntity.ok(customerFacade.searchCustomersByEmail(email, pageable));
    }

    @PostMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadCustomerImage(@PathVariable String id, @RequestParam("image") MultipartFile file) throws IOException {
        log.info("Received request to upload image for customer with id: {}", id);
        customerFacade.updateCustomerImage(id, file.getBytes());
        return ResponseEntity.ok("Image uploaded successfully");
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getCustomerImage(@PathVariable String id) {
        log.info("Received request to get image for customer with id: {}", id);
        byte[] image = customerFacade.getCustomerImage(id);
        if (image == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // Assuming JPEG, but could be dynamic
                .body(image);
    }

    @DeleteMapping("/{id}/image")
    public ResponseEntity<String> deleteCustomerImage(@PathVariable String id) {
        log.info("Received request to delete image for customer with id: {}", id);
        customerFacade.deleteCustomerImage(id);
        return ResponseEntity.ok("Image deleted successfully");
    }
}
