package com.example.loantraking.service;

import com.example.loantraking.Entity.CustomerAttachment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerAttachmentService {
    CustomerAttachment saveAttachment(CustomerAttachment attachment);
    List<CustomerAttachment> getAttachmentsByCustomerId(String customerId);
    Page<CustomerAttachment> getAttachmentsByCustomerId(String customerId, Pageable pageable);
    CustomerAttachment getAttachmentById(Long id);
    void deleteAttachment(Long id);
}
