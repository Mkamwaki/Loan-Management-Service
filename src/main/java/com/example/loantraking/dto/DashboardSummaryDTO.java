package com.example.loantraking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryDTO {
    private long totalDisbursedLoans;
    private long totalApprovedLoans;
    private long totalSentForApprovalLoans;
    private long totalPendingApprovalLoans; // approved + sent for approval
    private long totalCustomers;
}
