package com.example.loantraking.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InstallmentRequestDTO {
    private String loanNumber;
    private BigDecimal amount;
}

