package com.example.loantraking.controller;

import com.example.loantraking.dto.PaymentScheduleResponseDTO;
import com.example.loantraking.service.PaymentScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Payment Schedule", description = "APIs for viewing loan payment schedules")
public class PaymentScheduleController {

    private final PaymentScheduleService paymentScheduleService;

    @Operation(summary = "Get payment schedule by loan number")
    @GetMapping("/{loanNumber}")
    public ResponseEntity<List<PaymentScheduleResponseDTO>> getByLoanNumber(@PathVariable String loanNumber) {
        return ResponseEntity.ok(paymentScheduleService.getByLoanNumber(loanNumber));
    }
}
