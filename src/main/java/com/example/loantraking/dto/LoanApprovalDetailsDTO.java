package com.example.loantraking.dto;

import com.example.loantraking.enums.loan_status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanApprovalDetailsDTO {
    private String userId;
    private loan_status status;
    private String comment;
    private LocalDateTime time;
}
