package com.example.loantraking.service;

import com.example.loantraking.Entity.PaymentSchedule;
import com.example.loantraking.dto.PaymentScheduleResponseDTO;
import com.example.loantraking.repository.PaymentScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentScheduleService {

    private final PaymentScheduleRepository paymentScheduleRepository;

    @Transactional(readOnly = true)
    public List<PaymentScheduleResponseDTO> getByLoanNumber(String loanNumber) {
        if (loanNumber == null || loanNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Loan number is required");
        }

        return paymentScheduleRepository.findByLoan_LoanNumberOrderByPaymentNoAsc(loanNumber).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private PaymentScheduleResponseDTO toResponse(PaymentSchedule schedule) {
        return PaymentScheduleResponseDTO.builder()
                .id(schedule.getId())
                .loanNumber(schedule.getLoan() != null ? schedule.getLoan().getLoanNumber() : null)
                .paymentDate(schedule.getPaymentDate())
                .paymentNo(schedule.getPaymentNo())
                .expectedAmount(schedule.getExpectedAmount())
                .status(schedule.getStatus())
                .createdBy(schedule.getCreatedBy())
                .createdDate(schedule.getCreatedDate())
                .build();
    }
}
