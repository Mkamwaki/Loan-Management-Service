package com.example.loantraking.controller;

import com.example.loantraking.dto.ExpectedIncomeDTO;
import com.example.loantraking.service.FinanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/loan/finance")
@RequiredArgsConstructor
@Tag(name = "Finance Operations", description = "APIs for financial calculations and summaries")
public class FinanceController {

    private final FinanceService financeService;

    @Operation(summary = "Get expected income from accrued interest")
    @GetMapping("/expected-income")
    public ResponseEntity<ExpectedIncomeDTO> getExpectedIncome() {
        return ResponseEntity.ok(financeService.getExpectedIncomeFromAccruedInterest());
    }
}

