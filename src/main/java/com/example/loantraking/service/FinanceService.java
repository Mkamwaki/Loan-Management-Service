package com.example.loantraking.service;

import com.example.loantraking.dto.ExpectedIncomeDTO;
import com.example.loantraking.repository.loanFinanceRepository;
import com.example.loantraking.enums.loan_status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class FinanceService {

    private final loanFinanceRepository loanFinanceRepository;

    @Transactional(readOnly = true)
    public ExpectedIncomeDTO getExpectedIncomeFromAccruedInterest() {
        BigDecimal sum = loanFinanceRepository.sumAccruedInterestByStatus(loan_status.DISBURSED);
        if (sum == null) sum = BigDecimal.ZERO;

        BigDecimal disbursed = loanFinanceRepository.sumDisbursedAmountByStatus(loan_status.DISBURSED);
        if (disbursed == null) disbursed = BigDecimal.ZERO;

        return ExpectedIncomeDTO.builder().expectedIncome(sum).totalDisbursedAmount(disbursed).build();
    }
}
