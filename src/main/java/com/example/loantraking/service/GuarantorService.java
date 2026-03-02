package com.example.loantraking.service;

import com.example.loantraking.Entity.Guarantor;
import com.example.loantraking.Entity.LoanInfo;
import com.example.loantraking.dto.GuarantorDTO;
import com.example.loantraking.repository.GuarantorRepository;
import com.example.loantraking.repository.LoanInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GuarantorService {

    private final GuarantorRepository guarantorRepository;
    private final LoanInfoRepository loanInfoRepository;

    @Transactional
    public List<Guarantor> addGuarantors(String loanNumber, List<GuarantorDTO> guarantorDTOs) {
        LoanInfo loan = loanInfoRepository.findByLoanNumber(loanNumber)
                .orElseThrow(() -> new RuntimeException("Loan not found with number: " + loanNumber));

        List<Guarantor> guarantors = guarantorDTOs.stream().map(dto -> 
            Guarantor.builder()
                    .loan(loan)
                    .fullName(dto.getFullName())
                    .email(dto.getEmail())
                    .phoneNumber(dto.getPhoneNumber())
                    .address(dto.getAddress())
                    .nationalId(dto.getNationalId())
                    .relationship(dto.getRelationship())
                    .amountGuaranteed(dto.getAmountGuaranteed())
                    .build()
        ).collect(Collectors.toList());

        return guarantorRepository.saveAll(guarantors);
    }

    public List<Guarantor> getGuarantorsByLoanNumber(String loanNumber) {
        return guarantorRepository.findByLoanLoanNumber(loanNumber);
    }
}
