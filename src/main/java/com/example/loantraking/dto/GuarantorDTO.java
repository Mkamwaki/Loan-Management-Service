package com.example.loantraking.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuarantorDTO {
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
    private String nationalId;
    private String relationship;
    private BigDecimal amountGuaranteed;

}
