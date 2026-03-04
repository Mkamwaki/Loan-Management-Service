package com.example.loantraking.service;

import com.example.loantraking.Entity.Installments;
import com.example.loantraking.Entity.LoanInfo;
import com.example.loantraking.Entity.loanFinance;
import com.example.loantraking.dto.InstallmentRequestDTO;
import com.example.loantraking.dto.InstallmentResponseDTO;
import com.example.loantraking.repository.InstallmentsRepository;
import com.example.loantraking.repository.LoanInfoRepository;
import com.example.loantraking.repository.loanFinanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstallmentsService {

    private final InstallmentsRepository installmentsRepository;
    private final LoanInfoRepository loanInfoRepository;
    private final loanFinanceRepository loanFinanceRepository;

    @Transactional
    public InstallmentResponseDTO addInstallment(InstallmentRequestDTO request) {
        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        LoanInfo loan = loanInfoRepository.findByLoanNumber(request.getLoanNumber())
                .orElseThrow(() -> new RuntimeException("Loan not found with number: " + request.getLoanNumber()));

        // create installment
        Installments installment = Installments.builder()
                .loan(loan)
                .amount(request.getAmount())
                .build();

        // Set createdBy from authenticated user (if available)
        String userEmail = resolveUserEmailFromJwt();
        if (userEmail == null) {
            throw new RuntimeException("User email not found in JWT");
        }
        installment.setCreatedBy(userEmail);

        Installments saved = installmentsRepository.save(installment);

        // update loan finance
        loanFinance finance = loanFinanceRepository.findByLoan_LoanNumber(loan.getLoanNumber())
                .orElseGet(() -> loanFinance.builder().loan(loan).build());

        BigDecimal paid = finance.getPaidAmount() != null ? finance.getPaidAmount() : BigDecimal.ZERO;
        BigDecimal outstanding = finance.getOutstandingAmount() != null ? finance.getOutstandingAmount() : BigDecimal.ZERO;

        // add to paid
        BigDecimal newPaid = paid.add(request.getAmount());

        // subtract from outstanding but do not go below zero
        BigDecimal newOutstanding = outstanding.subtract(request.getAmount());
        if (newOutstanding.compareTo(BigDecimal.ZERO) < 0) {
            newOutstanding = BigDecimal.ZERO;
        }

        finance.setPaidAmount(newPaid);
        finance.setOutstandingAmount(newOutstanding);
        // set last modified by to the current user if available
        finance.setLastModifiedBy(userEmail);

        loanFinanceRepository.save(finance);

        return InstallmentResponseDTO.builder()
                .id(saved.getId())
                .loanNumber(loan.getLoanNumber())
                .amount(saved.getAmount())
                .createdBy(saved.getCreatedBy())
                .createdDate(saved.getCreatedDate())
                .build();
    }

    @Transactional(readOnly = true)
    public List<InstallmentResponseDTO> getInstallmentsByLoanNumber(String loanNumber) {
        List<Installments> installments = installmentsRepository.findByLoan_LoanNumber(loanNumber);
        return installments.stream().map(i -> InstallmentResponseDTO.builder()
                .id(i.getId())
                .loanNumber(i.getLoan() != null ? i.getLoan().getLoanNumber() : null)
                .amount(i.getAmount())
                .createdBy(i.getCreatedBy())
                .createdDate(i.getCreatedDate())
                .build()).collect(Collectors.toList());
    }

    private String resolveUserEmailFromJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Jwt jwt = null;
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            jwt = jwtAuth.getToken();
        } else if (authentication.getPrincipal() instanceof Jwt principalJwt) {
            jwt = principalJwt;
        }

        if (jwt == null) {
            return null;
        }

        Object claim = jwt.getClaims().get("email");
        if (claim == null) claim = jwt.getClaims().get("preferred_username");
        if (claim == null) claim = jwt.getClaims().get("upn");
        if (claim == null) claim = jwt.getClaims().get("sub");

        if (claim instanceof String email) {
            return email;
        }

        return null;
    }
}
