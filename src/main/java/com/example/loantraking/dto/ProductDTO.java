package com.example.loantraking.dto;

import com.example.loantraking.enums.currency;
import com.example.loantraking.enums.interestType;
import com.example.loantraking.enums.productSecurity;
import com.example.loantraking.enums.productstatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    private Long productId;
    private String productCode;
    private String productName;
    private String productType;
    private String description;
    private currency currency;
    private BigDecimal minimumAmount;
    private BigDecimal maximumAmount;
    private BigDecimal defaultInterestRate;
    private interestType interestType;
    private Integer minimumTermMonths;
    private Integer maximumTermMonths;
    private String repaymentFrequency;
    private productSecurity security;
    private BigDecimal processingFee;
    private BigDecimal latePaymentFee;
    private BigDecimal prepaymentPenalty;
    private productstatus status;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    private String microfinanceId;

    // audit fields to display
    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;
}
