package com.example.loantraking.service;

import com.example.loantraking.Entity.CustomerAttachment;
import com.example.loantraking.repository.CustomerAttachmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerAttachmentServiceImpl implements CustomerAttachmentService {

    private final CustomerAttachmentRepository attachmentRepository;

    @Override
    public CustomerAttachment saveAttachment(CustomerAttachment attachment) {
        log.info("Saving customer attachment: {}", attachment.getName());
        return attachmentRepository.save(attachment);
    }

    @Override
    public List<CustomerAttachment> getAttachmentsByCustomerId(String customerId) {
        log.info("Fetching attachments for customer: {}", customerId);
        return attachmentRepository.findByCustomerCustomerId(customerId);
    }

    @Override
    public Page<CustomerAttachment> getAttachmentsByCustomerId(String customerId, Pageable pageable) {
        log.info("Fetching attachments for customer: {} with pagination: {}", customerId, pageable);
        return attachmentRepository.findByCustomerCustomerId(customerId, pageable);
    }

    @Override
    public CustomerAttachment getAttachmentById(Long id) {
        log.info("Fetching attachment by id: {}", id);
        return attachmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attachment not found with id: " + id));
    }

    @Override
    public void deleteAttachment(Long id) {
        log.info("Deleting attachment by id: {}", id);
        attachmentRepository.deleteById(id);
    }
}
