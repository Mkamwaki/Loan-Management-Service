package com.example.loantraking.controller;

import com.example.loantraking.dto.InstallmentRequestDTO;
import com.example.loantraking.dto.InstallmentResponseDTO;
import com.example.loantraking.service.InstallmentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/loan/installments")
@RequiredArgsConstructor
public class InstallmentsController {

    private final InstallmentsService installmentsService;

    @PostMapping
    public ResponseEntity<InstallmentResponseDTO> addInstallment(@Valid @RequestBody InstallmentRequestDTO request) {
        try {
            InstallmentResponseDTO response = installmentsService.addInstallment(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/pay")
    public ResponseEntity<InstallmentResponseDTO> addInstallmentAndMarkPaid(@Valid @RequestBody InstallmentRequestDTO request) {
        try {
            InstallmentResponseDTO response = installmentsService.addInstallmentAndMarkSchedulePaid(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{loanNumber}")
    public ResponseEntity<List<InstallmentResponseDTO>> getInstallments(@PathVariable String loanNumber) {
        List<InstallmentResponseDTO> list = installmentsService.getInstallmentsByLoanNumber(loanNumber);
        return ResponseEntity.ok(list);
    }
}
