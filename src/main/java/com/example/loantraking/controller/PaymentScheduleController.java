package com.example.loantraking.controller;

import com.example.loantraking.dto.PaymentScheduleResponseDTO;
import com.example.loantraking.service.PaymentScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/loan/payment-schedule")
@RequiredArgsConstructor
public class PaymentScheduleController {

    private final PaymentScheduleService paymentScheduleService;

    @GetMapping("/{loanNumber}")
    public ResponseEntity<List<PaymentScheduleResponseDTO>> getByLoanNumber(@PathVariable String loanNumber) {
        return ResponseEntity.ok(paymentScheduleService.getByLoanNumber(loanNumber));
    }
}
