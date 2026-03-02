package com.example.loantraking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollateralsDTO {
    private Long id;
    private String loanNumber;
    private String customerId;
    private String type;
    private String registrationNumber;
    private String description;
    private BigDecimal estimatedValue;
    
    private List<CollateralAttachmentDTO> attachments;
    
    // Audit fields
    private String createdBy;
    private LocalDateTime createdDate;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;
}
