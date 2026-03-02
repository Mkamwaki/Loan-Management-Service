package com.example.loantraking.dto;

import com.example.loantraking.enums.loan_status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanDetailsDTO {
    private String loanNumber;
    private String applicantName;
    private BigDecimal amountRequested;
    private BigDecimal expectedAmountToPay;
    private Integer tenureMonths;
    private BigDecimal interestRatePercent;
    private String repaymentFrequency;
    private String productName;
    private loan_status status;
}
