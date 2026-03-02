package com.example.loantraking.controller;

import com.example.loantraking.dto.LoanApprovalDetailsDTO;
import com.example.loantraking.service.LoanApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/loan")
@RequiredArgsConstructor
public class LoanApprovalController {

    private final LoanApprovalService loanApprovalService;

    @GetMapping("approval/{referenceId}")
    public ResponseEntity<List<LoanApprovalDetailsDTO>> getApprovalDetailsByReferenceId(@PathVariable String referenceId) {
        return ResponseEntity.ok(loanApprovalService.getApprovalDetailsByReferenceId(referenceId));
    }
}
