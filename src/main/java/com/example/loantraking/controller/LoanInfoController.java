package com.example.loantraking.controller;

import com.example.loantraking.Entity.LoanInfo;
import com.example.loantraking.dto.*;
import com.example.loantraking.service.LoanInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.loantraking.enums.loan_status;

import java.util.List;

@RestController
@RequestMapping("/loan")
@RequiredArgsConstructor
@Tag(name = "Loan Management", description = "APIs for loan applications, updates, and tracking")
public class LoanInfoController {

    private final LoanInfoService loanInfoService;

    @Operation(summary = "Apply for a new loan")
    @PostMapping("/apply")
    public ResponseEntity<LoanApplicationResponse> applyForLoan(@RequestBody LoanApplicationRequest request) {
        LoanInfo loanInfo = loanInfoService.createLoanApplication(request);
        
        LoanApplicationResponse response = LoanApplicationResponse.builder()
                .message("loan number created successfully and the number is " + loanInfo.getLoanNumber())
                .build();
                
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all loans with pagination")
    @GetMapping("/all")
    public ResponseEntity<Page<LoanDetailsDTO>> getAllLoans(Pageable pageable) {
        return ResponseEntity.ok(loanInfoService.getAllLoanDetails(pageable));
    }

    @Operation(summary = "Get loans by status with pagination")
    @GetMapping("/status")
    public ResponseEntity<Page<LoanDetailsDTO>> getLoansByStatus(@RequestParam("status") loan_status status,
                                                                 Pageable pageable) {
        return ResponseEntity.ok(loanInfoService.getLoanDetailsByStatus(status, pageable));
    }

    @Operation(summary = "Get loan summary by loan number")
    @GetMapping("/summary/{loanNumber}")
    public ResponseEntity<LoanSummaryDTO> getLoanSummary(@PathVariable String loanNumber) {
        return ResponseEntity.ok(loanInfoService.getLoanSummaryByLoanNumber(loanNumber));
    }

    @Operation(summary = "Update loan information by loan number")
    @PutMapping("/update/{loanNumber}")
    public ResponseEntity<?> updateLoanByNumber(@PathVariable String loanNumber, @RequestBody LoanUpdateDTO dto) {
        try {
            loanInfoService.updateLoanByNumber(loanNumber, dto);
            return ResponseEntity.ok("Loan updated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Get loan update details by loan number")
    @GetMapping("/view/{loanNumber}")
    public ResponseEntity<LoanUpdateDTO> getLoanByNumber(@PathVariable String loanNumber) {
        try {
            return ResponseEntity.ok(loanInfoService.getLoanUpdateDTOByNumber(loanNumber));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Update loan status (approved, rejected, etc.)")
    @PostMapping("/update-status")
    public ResponseEntity<?> updateLoanStatus(@RequestBody LoanStatusUpdateDTO dto) {
        try {
            loanInfoService.updateLoanStatus(dto);
            return ResponseEntity.ok("Loan status updated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
