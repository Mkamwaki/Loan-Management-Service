package com.example.loantraking.service;

import com.example.loantraking.dto.DashboardSummaryDTO;
import com.example.loantraking.enums.loan_status;
import com.example.loantraking.repository.CustomerRepository;
import com.example.loantraking.repository.LoanInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final LoanInfoRepository loanInfoRepository;
    private final CustomerRepository customerRepository;

    @Transactional(readOnly = true)
    public DashboardSummaryDTO getSummary() {
        long disbursed = loanInfoRepository.countByStatus(loan_status.DISBURSED);
        long approved = loanInfoRepository.countByStatus(loan_status.APPROVED);
        long sentForApproval = loanInfoRepository.countByStatus(loan_status.SENT_FOR_APPROVAL);
        long pendingForApproval = approved + sentForApproval;
        long customers = customerRepository.count();

        return DashboardSummaryDTO.builder()
                .totalDisbursedLoans(disbursed)
                .totalApprovedLoans(approved)
                .totalSentForApprovalLoans(sentForApproval)
                .totalPendingApprovalLoans(pendingForApproval)
                .totalCustomers(customers)
                .build();
    }
}
