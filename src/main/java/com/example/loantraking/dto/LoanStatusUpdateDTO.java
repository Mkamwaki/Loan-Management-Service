package com.example.loantraking.dto;

import com.example.loantraking.enums.loan_status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanStatusUpdateDTO {
    private String loanNumber;
    private loan_status status;
    private String comment;
}
