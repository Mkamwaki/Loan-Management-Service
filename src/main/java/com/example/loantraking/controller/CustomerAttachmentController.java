package com.example.loantraking.controller;

import com.example.loantraking.dto.CustomerAttachmentDTO;
import com.example.loantraking.facade.CustomerAttachmentFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("loan/customer-attachments")
@RequiredArgsConstructor
@Slf4j
public class CustomerAttachmentController {

    private final CustomerAttachmentFacade attachmentFacade;

    @PostMapping
    public ResponseEntity<CustomerAttachmentDTO> addAttachment(@Valid @RequestBody CustomerAttachmentDTO attachmentDTO) {
        log.info("Received request to add attachment: {}", attachmentDTO);
        return ResponseEntity.ok(attachmentFacade.addAttachment(attachmentDTO));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Page<CustomerAttachmentDTO>> getAttachmentsByCustomerId(
            @PathVariable String customerId,
            @PageableDefault(size = 10) Pageable pageable) {
        log.info("Received request to get attachments for customer with id: {}", customerId);
        return ResponseEntity.ok(attachmentFacade.getAttachmentsByCustomerId(customerId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerAttachmentDTO> getAttachment(@PathVariable Long id) {
        log.info("Received request to get attachment with id: {}", id);
        return ResponseEntity.ok(attachmentFacade.getAttachment(id));
    }

    @GetMapping("/{id}/view")
    public ResponseEntity<byte[]> viewAttachment(@PathVariable Long id) {
        log.info("Received request to view attachment with id: {}", id);
        CustomerAttachmentDTO attachment = attachmentFacade.getAttachment(id);
        byte[] content = attachmentFacade.getAttachmentContent(id);
        
        if (content == null) {
            return ResponseEntity.notFound().build();
        }

        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
        String fileName = attachment.getName().toLowerCase();
        if (fileName.endsWith(".png")) {
            mediaType = MediaType.IMAGE_PNG;
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            mediaType = MediaType.IMAGE_JPEG;
        } else if (fileName.endsWith(".pdf")) {
            mediaType = MediaType.APPLICATION_PDF;
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + attachment.getName() + "\"")
                .contentType(mediaType)
                .body(content);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAttachment(@PathVariable Long id) {
        log.info("Received request to delete attachment with id: {}", id);
        attachmentFacade.deleteAttachment(id);
        return ResponseEntity.ok("Attachment deleted successfully");
    }
}
