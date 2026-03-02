package com.example.loantraking.dto;

import com.example.loantraking.enums.currency;
import com.example.loantraking.enums.interestType;
import com.example.loantraking.enums.productSecurity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanUpdateDTO {
    private String productName;
    private String productType;
    private currency currency;
    private productSecurity security;
    private BigDecimal loanAmount;
    private Integer tenureMonths;
    private BigDecimal interestRatePercent;
    private String repaymentFrequency;
    private interestType interestType;
    private BigDecimal processingFeeTZS;
    private BigDecimal latePaymentFeeTZS;
    private BigDecimal prepaymentPenaltyPercent;
    
    // Financial fields
    private BigDecimal accruedInterest;
    private BigDecimal outstandingAmount;
    private BigDecimal paidAmount;
    private BigDecimal installmentAmount;
    private BigDecimal totalAmountToBePaid;
}
