package com.example.loantraking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpectedIncomeDTO {
    private BigDecimal expectedIncome;
    private BigDecimal totalDisbursedAmount;
}
