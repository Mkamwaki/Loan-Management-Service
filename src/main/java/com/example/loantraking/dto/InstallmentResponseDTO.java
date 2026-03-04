package com.example.loantraking.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class InstallmentResponseDTO {
    private Long id;
    private String loanNumber;
    private BigDecimal amount;
    private String createdBy;
    private LocalDateTime createdDate;
}

