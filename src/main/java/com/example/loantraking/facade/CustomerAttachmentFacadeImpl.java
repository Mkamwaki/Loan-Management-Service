package com.example.loantraking.facade;

import com.example.loantraking.Entity.Customer;
import com.example.loantraking.Entity.CustomerAttachment;
import com.example.loantraking.dto.CustomerAttachmentDTO;
import com.example.loantraking.service.CustomerAttachmentService;
import com.example.loantraking.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CustomerAttachmentFacadeImpl implements CustomerAttachmentFacade {

    private final CustomerAttachmentService attachmentService;
    private final CustomerService customerService;
    private static final String UPLOAD_DIR = "uploads/attachments/";

    @Override
    public CustomerAttachmentDTO addAttachment(CustomerAttachmentDTO attachmentDTO) {
        Customer customer = customerService.getCustomerById(attachmentDTO.getCustomerId());
        
        String filePath = null;
        if (attachmentDTO.getFileContent() != null && !attachmentDTO.getFileContent().isEmpty()) {
            filePath = saveFile(attachmentDTO.getFileContent(), attachmentDTO.getName());
            attachmentDTO.setPath(filePath);
        }

        CustomerAttachment attachment = convertToEntity(attachmentDTO, customer);
        CustomerAttachment savedAttachment = attachmentService.saveAttachment(attachment);
        return convertToDTO(savedAttachment);
    }

    private String saveFile(String base64Content, String originalName) {
        try {
            // Create uploads directory if it doesn't exist
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Decode Base64 content
            byte[] decodedBytes = Base64.getDecoder().decode(base64Content);

            // Generate unique filename
            String fileName = UUID.randomUUID().toString() + "_" + originalName.trim().replaceAll("[\\\\/:*?\"<>|]", "_");
            Path filePath = uploadPath.resolve(fileName);

            // Save file to disk
            Files.write(filePath, decodedBytes);

            return filePath.toString();
        } catch (IOException e) {
            throw new RuntimeException("Could not store file. Error: " + e.getMessage());
        }
    }

    @Override
    public List<CustomerAttachmentDTO> getAttachmentsByCustomerId(String customerId) {
        return attachmentService.getAttachmentsByCustomerId(customerId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<CustomerAttachmentDTO> getAttachmentsByCustomerId(String customerId, Pageable pageable) {
        return attachmentService.getAttachmentsByCustomerId(customerId, pageable)
                .map(this::convertToDTO);
    }

    @Override
    public CustomerAttachmentDTO getAttachment(Long id) {
        CustomerAttachment attachment = attachmentService.getAttachmentById(id);
        return convertToDTO(attachment);
    }

    @Override
    public byte[] getAttachmentContent(Long id) {
        CustomerAttachment attachment = attachmentService.getAttachmentById(id);
        if (attachment.getPath() != null) {
            try {
                Path filePath = Paths.get(attachment.getPath());
                if (Files.exists(filePath)) {
                    return Files.readAllBytes(filePath);
                }
            } catch (IOException e) {
                throw new RuntimeException("Could not read file for attachment " + id + ". Error: " + e.getMessage());
            }
        }
        return null;
    }

    @Override
    public void deleteAttachment(Long id) {
        attachmentService.deleteAttachment(id);
    }

    private CustomerAttachment convertToEntity(CustomerAttachmentDTO dto, Customer customer) {
        return CustomerAttachment.builder()
                .name(dto.getName())
                .path(dto.getPath())
                .customer(customer)
                .build();
    }

    private CustomerAttachmentDTO convertToDTO(CustomerAttachment entity) {
        return CustomerAttachmentDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .path(entity.getPath())
                .customerId(entity.getCustomer().getCustomerId())
                .createdBy(entity.getCreatedBy())
                .createdDate(entity.getCreatedDate())
                .lastModifiedBy(entity.getLastModifiedBy())
                .lastModifiedDate(entity.getLastModifiedDate())
                .build();
    }
}
