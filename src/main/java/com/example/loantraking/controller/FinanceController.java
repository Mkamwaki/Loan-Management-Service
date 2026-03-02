package com.example.loantraking.controller;

import com.example.loantraking.dto.ExpectedIncomeDTO;
import com.example.loantraking.service.FinanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/loan/finance")
@RequiredArgsConstructor
public class FinanceController {

    private final FinanceService financeService;

    @GetMapping("/expected-income")
    public ResponseEntity<ExpectedIncomeDTO> getExpectedIncome() {
        return ResponseEntity.ok(financeService.getExpectedIncomeFromAccruedInterest());
    }
}

