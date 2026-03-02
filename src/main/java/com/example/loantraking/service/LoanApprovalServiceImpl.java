package com.example.loantraking.service;

import com.example.loantraking.dto.LoanApprovalDetailsDTO;
import com.example.loantraking.repository.LoanApprovalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanApprovalServiceImpl implements LoanApprovalService {

    private final LoanApprovalRepository loanApprovalRepository;

    @Override
    public List<LoanApprovalDetailsDTO> getApprovalDetailsByReferenceId(String referenceId) {
        return loanApprovalRepository.findByReferenceId(referenceId).stream()
                .map(approval -> LoanApprovalDetailsDTO.builder()
                        .userId(approval.getUserId())
                        .status(approval.getStatus())
                        .time(approval.getCreatedDate())
                        .build())
                .toList();
    }
}
