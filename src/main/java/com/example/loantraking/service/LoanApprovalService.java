package com.example.loantraking.service;

import com.example.loantraking.dto.LoanApprovalDetailsDTO;

import java.util.List;

public interface LoanApprovalService {
    List<LoanApprovalDetailsDTO> getApprovalDetailsByReferenceId(String referenceId);
}
