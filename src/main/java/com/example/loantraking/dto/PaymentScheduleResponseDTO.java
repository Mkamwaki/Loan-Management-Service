package com.example.loantraking.dto;

import com.example.loantraking.enums.PaymentScheduleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentScheduleResponseDTO {
    private Long id;
    private String loanNumber;
    private LocalDate paymentDate;
    private Integer paymentNo;
    private BigDecimal expectedAmount;
    private PaymentScheduleStatus status;
    private String createdBy;
    private LocalDateTime createdDate;
}
