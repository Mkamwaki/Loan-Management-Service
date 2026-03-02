package com.example.loantraking.facade;

import com.example.loantraking.dto.CustomerAttachmentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerAttachmentFacade {
    CustomerAttachmentDTO addAttachment(CustomerAttachmentDTO attachmentDTO);
    List<CustomerAttachmentDTO> getAttachmentsByCustomerId(String customerId);
    Page<CustomerAttachmentDTO> getAttachmentsByCustomerId(String customerId, Pageable pageable);
    CustomerAttachmentDTO getAttachment(Long id);
    byte[] getAttachmentContent(Long id);
    void deleteAttachment(Long id);
}
