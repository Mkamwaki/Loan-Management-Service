package com.example.loantraking.service;

import com.example.loantraking.Entity.*;
import com.example.loantraking.dto.*;
import com.example.loantraking.enums.loan_status;
import com.example.loantraking.facade.CollateralsFacade;
import com.example.loantraking.repository.CustomerRepository;
import com.example.loantraking.repository.LoanApprovalRepository;
import com.example.loantraking.repository.LoanInfoRepository;
import com.example.loantraking.repository.PaymentScheduleRepository;
import com.example.loantraking.repository.loanFinanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoanInfoService {

    private final LoanInfoRepository loanInfoRepository;
    private final CustomerRepository customerRepository;
    private final loanFinanceRepository loanFinanceRepository;
    private final LoanApprovalRepository loanApprovalRepository;
    private final PaymentScheduleRepository paymentScheduleRepository;
    private final GuarantorService guarantorService;
    private final CollateralsFacade collateralsFacade;
    private final SecureRandom random = new SecureRandom();

    @Transactional
    public LoanInfo createLoanApplication(LoanApplicationRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + request.getCustomerId()));

        String loanNumber = generateUniqueLoanNumber();

        LoanInfo loanInfo = LoanInfo.builder()
                .customer(customer)
                .loanNumber(loanNumber)
                .status(loan_status.PENDING)
                // Audit fields are handled by JPA Auditing
                .build();

        return loanInfoRepository.save(loanInfo);
    }

    public Page<LoanDetailsDTO> getAllLoanDetails(Pageable pageable) {
        return loanInfoRepository.findAll(pageable)
                .map(this::convertToLoanDetailsDTO);
    }

    @Transactional(readOnly = true)
    public Page<LoanDetailsDTO> getLoanDetailsByStatus(loan_status status, Pageable pageable) {
        return loanInfoRepository.findByStatus(status, pageable)
                .map(this::convertToLoanDetailsDTO);
    }

    @Transactional(readOnly = true)
    public LoanSummaryDTO getLoanSummaryByLoanNumber(String loanNumber) {
        LoanInfo loanInfo = loanInfoRepository.findByLoanNumber(loanNumber)
                .orElseThrow(() -> new RuntimeException("Loan not found with number: " + loanNumber));

        Customer customer = loanInfo.getCustomer();

        List<RelativeDTO> relatives = customer.getRelatives().stream()
                .map(this::convertToRelativeDTO)
                .collect(Collectors.toList());

        List<CustomerAttachmentDTO> attachments = customer.getAttachments().stream()
                .map(this::convertToAttachmentDTO)
                .collect(Collectors.toList());

        List<GuarantorDTO> guarantors = guarantorService.getGuarantorsByLoanNumber(loanNumber).stream()
                .map(this::convertToGuarantorDTO)
                .collect(Collectors.toList());

        List<CollateralsDTO> collaterals = collateralsFacade.getByLoanNumber(loanNumber);

        LoanUpdateDTO loanDetails = getLoanUpdateDTOByNumber(loanNumber);

        return LoanSummaryDTO.builder()
                .applicationId(loanInfo.getLoanNumber())
                .applicantName(customer.getFirstName() + " " + customer.getLastName())
                .customerId(customer.getCustomerId())
                .id(customer.getCustomerId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .phoneNumber(customer.getPhoneNumber())
                .address(customer.getAddress())
                .nationalId(customer.getNationalId())
                .status(customer.getStatus() != null ? customer.getStatus().name() : null)
                .loanStatus(loanInfo.getStatus() != null ? loanInfo.getStatus().name() : null)
                .createdBy(customer.getCreatedBy())
                .createdDate(customer.getCreatedDate())
                .updatedBy(customer.getUpdatedBy())
                .updatedDate(customer.getUpdatedDate())
                .relatives(relatives)
                .customerAttachments(attachments)
                .guarantors(guarantors)
                .collaterals(collaterals)
                .loanDetails(loanDetails)
                .build();
    }

    @Transactional
    public void updateLoanStatus(LoanStatusUpdateDTO dto) {
        LoanInfo loanInfo = loanInfoRepository.findByLoanNumber(dto.getLoanNumber())
                .orElseThrow(() -> new RuntimeException("Loan not found with number: " + dto.getLoanNumber()));

        loanInfo.setStatus(dto.getStatus());
        loanInfoRepository.save(loanInfo);

        if (dto.getStatus() == loan_status.DISBURSED) {
            createDisbursedPaymentSchedule(loanInfo);
        }

        String userId = resolveUserIdFromJwt();
        if (userId == null) {
            throw new RuntimeException("User id not found in JWT");
        }

        LoanApproval approval = LoanApproval.builder()
                .referenceId(loanInfo.getLoanNumber())
                .loanInfo(loanInfo)
                .userId(userId)
                .status(dto.getStatus())
                .comment(dto.getComment())
                .build();

        loanApprovalRepository.save(approval);
    }

    private void createDisbursedPaymentSchedule(LoanInfo loanInfo) {
        Integer loanTermMonths = loanInfo.getLoanTermMonths();
        if (loanTermMonths == null || loanTermMonths <= 0) {
            throw new RuntimeException("Cannot create payment schedule. Loan term must be greater than zero.");
        }

        BigDecimal totalPayableAmount = resolveTotalPayableAmount(loanInfo);
        if (totalPayableAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Cannot create payment schedule. Total payable amount must be greater than zero.");
        }

        LocalDate disbursedDate = LocalDate.now();
        LocalDate maturityDate = disbursedDate.plusMonths(loanTermMonths);
        List<LocalDate> paymentDates = buildPaymentDates(disbursedDate, maturityDate, loanInfo.getRepaymentFrequency());

        paymentScheduleRepository.deleteByLoan_LoanNumber(loanInfo.getLoanNumber());

        int totalPayments = paymentDates.size();
        BigDecimal baseExpectedAmount = totalPayableAmount
                .divide(BigDecimal.valueOf(totalPayments), 4, RoundingMode.DOWN);

        BigDecimal allocatedAmount = BigDecimal.ZERO;
        List<PaymentSchedule> scheduleRows = new ArrayList<>(totalPayments);
        for (int i = 0; i < totalPayments; i++) {
            BigDecimal expectedAmount;
            if (i == totalPayments - 1) {
                expectedAmount = totalPayableAmount.subtract(allocatedAmount).setScale(4, RoundingMode.HALF_UP);
            } else {
                expectedAmount = baseExpectedAmount;
                allocatedAmount = allocatedAmount.add(expectedAmount);
            }

            scheduleRows.add(PaymentSchedule.builder()
                    .loan(loanInfo)
                    .paymentDate(paymentDates.get(i))
                    .paymentNo(i + 1)
                    .expectedAmount(expectedAmount)
                    .build());
        }

        paymentScheduleRepository.saveAll(scheduleRows);
    }

    private BigDecimal resolveTotalPayableAmount(LoanInfo loanInfo) {
        BigDecimal amountFromFinance = loanFinanceRepository.findByLoan_LoanNumber(loanInfo.getLoanNumber())
                .map(finance -> {
                    BigDecimal outstanding = finance.getOutstandingAmount() != null ? finance.getOutstandingAmount() : BigDecimal.ZERO;
                    BigDecimal paid = finance.getPaidAmount() != null ? finance.getPaidAmount() : BigDecimal.ZERO;
                    BigDecimal totalFromOutstanding = outstanding.add(paid);
                    if (totalFromOutstanding.compareTo(BigDecimal.ZERO) > 0) {
                        return totalFromOutstanding;
                    }

                    BigDecimal disbursed = finance.getDisbursedAmount() != null ? finance.getDisbursedAmount() : BigDecimal.ZERO;
                    BigDecimal accruedInterest = finance.getAccruedInterest() != null ? finance.getAccruedInterest() : BigDecimal.ZERO;
                    return disbursed.add(accruedInterest);
                })
                .orElse(BigDecimal.ZERO);

        if (amountFromFinance.compareTo(BigDecimal.ZERO) > 0) {
            return amountFromFinance;
        }

        BigDecimal principal = loanInfo.getPrincipalAmount() != null ? loanInfo.getPrincipalAmount() : BigDecimal.ZERO;
        if (principal.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Cannot create payment schedule. Loan amount is missing for loan: " + loanInfo.getLoanNumber());
        }

        BigDecimal interestRatePercent = loanInfo.getInterestRate() != null ? loanInfo.getInterestRate() : BigDecimal.ZERO;
        Integer tenure = loanInfo.getLoanTermMonths() != null ? loanInfo.getLoanTermMonths() : 0;
        if (tenure > 0 && interestRatePercent.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal interest = principal
                    .multiply(interestRatePercent)
                    .multiply(BigDecimal.valueOf(tenure))
                    .divide(BigDecimal.valueOf(1200), 15, RoundingMode.HALF_UP);
            return principal.add(interest).setScale(4, RoundingMode.HALF_UP);
        }

        return principal.setScale(4, RoundingMode.HALF_UP);
    }

    private List<LocalDate> buildPaymentDates(LocalDate disbursedDate, LocalDate maturityDate, String repaymentFrequency) {
        List<LocalDate> paymentDates = new ArrayList<>();
        LocalDate paymentDate = incrementPaymentDate(disbursedDate, repaymentFrequency);

        while (!paymentDate.isAfter(maturityDate)) {
            paymentDates.add(paymentDate);
            paymentDate = incrementPaymentDate(paymentDate, repaymentFrequency);
        }

        if (paymentDates.isEmpty()) {
            paymentDates.add(maturityDate);
        }

        return paymentDates;
    }

    private LocalDate incrementPaymentDate(LocalDate date, String repaymentFrequency) {
        String normalized = repaymentFrequency == null ? "" : repaymentFrequency.trim().toUpperCase(Locale.ROOT);

        return switch (normalized) {
            case "DAILY", "DAY", "PER_DAY" -> date.plusDays(1);
            case "WEEKLY", "WEEK" -> date.plusWeeks(1);
            case "BIWEEKLY", "BI-WEEKLY", "FORTNIGHTLY", "EVERY_2_WEEKS" -> date.plusWeeks(2);
            case "QUARTERLY" -> date.plusMonths(3);
            case "MONTHLY", "MONTH" -> date.plusMonths(1);
            default -> date.plusMonths(1);
        };
    }

    private String resolveUserIdFromJwt() {
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
        if (claim == null) claim = jwt.getClaims().get("user_id");
        if (claim == null) claim = jwt.getClaims().get("userId");
        if (claim == null) claim = jwt.getClaims().get("id");

        if (claim instanceof String str) {
            return str;
        }
        if (claim instanceof Number number) {
            return number.toString();
        }
        return null;
    }

    private CustomerAttachmentDTO convertToAttachmentDTO(CustomerAttachment attachment) {
        return CustomerAttachmentDTO.builder()
                .id(attachment.getId())
                .name(attachment.getName())
                .path(attachment.getPath())
                .customerId(attachment.getCustomer().getCustomerId())
                .createdBy(attachment.getCreatedBy())
                .createdDate(attachment.getCreatedDate())
                .lastModifiedBy(attachment.getLastModifiedBy())
                .lastModifiedDate(attachment.getLastModifiedDate())
                .build();
    }

    private GuarantorDTO convertToGuarantorDTO(Guarantor guarantor) {
        return GuarantorDTO.builder()
                .fullName(guarantor.getFullName())
                .email(guarantor.getEmail())
                .phoneNumber(guarantor.getPhoneNumber())
                .address(guarantor.getAddress())
                .nationalId(guarantor.getNationalId())
                .relationship(guarantor.getRelationship())
                .amountGuaranteed(guarantor.getAmountGuaranteed())
                .build();
    }

    private RelativeDTO convertToRelativeDTO(Relative relative) {
        return RelativeDTO.builder()
                .relativeId(relative.getRelativeId())
                .fullName(relative.getFullName())
                .email(relative.getEmail())
                .mobileNumber(relative.getMobileNumber())
                .nationalId(relative.getNationalId())
                .gender(relative.getGender())
                .dateOfBirth(relative.getDateOfBirth())
                .relationship(relative.getRelationship())
                .customerId(relative.getCustomer().getCustomerId())
                .createdBy(relative.getCreatedBy())
                .updatedBy(relative.getUpdatedBy())
                .createdDate(relative.getCreatedDate())
                .updatedDate(relative.getUpdatedDate())
                .build();
    }

    private LoanDetailsDTO convertToLoanDetailsDTO(LoanInfo loanInfo) {
        BigDecimal outstandingAmount = BigDecimal.ZERO;
        BigDecimal paidAmount = BigDecimal.ZERO;

        loanFinance finance = loanFinanceRepository.findByLoan_LoanNumber(loanInfo.getLoanNumber()).orElse(null);
        if (finance != null) {
            outstandingAmount = finance.getOutstandingAmount() != null ? finance.getOutstandingAmount() : BigDecimal.ZERO;
            paidAmount = finance.getPaidAmount() != null ? finance.getPaidAmount() : BigDecimal.ZERO;
        }

        return LoanDetailsDTO.builder()
                .loanNumber(loanInfo.getLoanNumber())
                .applicantName(loanInfo.getCustomer().getFirstName() + " " + loanInfo.getCustomer().getLastName())
                .amountRequested(loanInfo.getPrincipalAmount())
                .expectedAmountToPay(outstandingAmount.add(paidAmount))
                .tenureMonths(loanInfo.getLoanTermMonths())
                .interestRatePercent(loanInfo.getInterestRate())
                .repaymentFrequency(loanInfo.getRepaymentFrequency())
                .productName(loanInfo.getProductName())
                .status(loanInfo.getStatus())
                .build();
    }

    private String generateUniqueLoanNumber() {
        String loanNumber;
        do {
            loanNumber = String.format("%06d", random.nextInt(1000000));
        } while (loanInfoRepository.findByLoanNumber(loanNumber).isPresent());
        return loanNumber;
    }

    @Transactional
    public LoanInfo updateLoanByNumber(String loanNumber, LoanUpdateDTO dto) {
        LoanInfo loan = loanInfoRepository.findByLoanNumber(loanNumber)
                .orElseThrow(() -> new RuntimeException("Loan not found with number: " + loanNumber));

        if (dto.getProductName() != null) loan.setProductName(dto.getProductName());
        if (dto.getProductType() != null) loan.setProductType(dto.getProductType());
        if (dto.getCurrency() != null) loan.setCurrency(dto.getCurrency());
        if (dto.getSecurity() != null) loan.setSecurity(dto.getSecurity());
        if (dto.getLoanAmount() != null) loan.setPrincipalAmount(dto.getLoanAmount());
        if (dto.getTenureMonths() != null) loan.setLoanTermMonths(dto.getTenureMonths());
        if (dto.getInterestRatePercent() != null) loan.setInterestRate(dto.getInterestRatePercent());
        if (dto.getRepaymentFrequency() != null) loan.setRepaymentFrequency(dto.getRepaymentFrequency());
        if (dto.getInterestType() != null) loan.setInterestType(dto.getInterestType());
        if (dto.getProcessingFeeTZS() != null) loan.setProcessingFee(dto.getProcessingFeeTZS());
        if (dto.getLatePaymentFeeTZS() != null) loan.setLatePaymentFee(dto.getLatePaymentFeeTZS());
        if (dto.getPrepaymentPenaltyPercent() != null) loan.setPrepaymentPenalty(dto.getPrepaymentPenaltyPercent());

        loan = loanInfoRepository.save(loan);
        updateLoanFinance(loan);

        return loan;
    }

    private void updateLoanFinance(LoanInfo loan) {
        BigDecimal principal = loan.getPrincipalAmount() != null ? loan.getPrincipalAmount() : BigDecimal.ZERO;
        BigDecimal interestRatePercent = loan.getInterestRate() != null ? loan.getInterestRate() : BigDecimal.ZERO;
        Integer tenure = loan.getLoanTermMonths() != null ? loan.getLoanTermMonths() : 0;

        BigDecimal installmentAmount = BigDecimal.ZERO;
        BigDecimal accruedInterest = BigDecimal.ZERO;
        BigDecimal outstandingAmount = principal;

        if (tenure > 0 && interestRatePercent.compareTo(BigDecimal.ZERO) > 0) {
            // Flat/fixed interest (simple interest, non-reducing balance)
            BigDecimal interest = principal
                    .multiply(interestRatePercent)
                    .multiply(BigDecimal.valueOf(tenure))
                    .divide(BigDecimal.valueOf(1200), 15, RoundingMode.HALF_UP); // 1200 = 12 months * 100 percent

            BigDecimal totalAmount = principal.add(interest);

            accruedInterest = interest.setScale(2, RoundingMode.HALF_UP);
            installmentAmount = totalAmount.divide(BigDecimal.valueOf(tenure), 2, RoundingMode.HALF_UP);
            outstandingAmount = totalAmount.setScale(2, RoundingMode.HALF_UP);
        }

        loanFinance finance = loanFinanceRepository.findByLoan_LoanNumber(loan.getLoanNumber())
                .orElse(loanFinance.builder().loan(loan).build());

        finance.setDisbursedAmount(principal);
        // Initial outstanding amount includes interest, but we subtract what has already been paid
        BigDecimal currentPaidAmount = finance.getPaidAmount() != null ? finance.getPaidAmount() : BigDecimal.ZERO;
        finance.setOutstandingAmount(outstandingAmount.subtract(currentPaidAmount));
        finance.setAccruedInterest(accruedInterest);
        finance.setInstallmentAmount(installmentAmount);
        // paidAmount should be zero by default and not updated here unless it's a new record
        if (finance.getId() == null) {
            finance.setPaidAmount(BigDecimal.ZERO);
        }
        finance.setFees(loan.getProcessingFee());

        loanFinanceRepository.save(finance);
    }

    @Transactional(readOnly = true)
    public LoanUpdateDTO getLoanUpdateDTOByNumber(String loanNumber) {
        LoanInfo loan = loanInfoRepository.findByLoanNumber(loanNumber)
                .orElseThrow(() -> new RuntimeException("Loan not found with number: " + loanNumber));

        LoanUpdateDTO.LoanUpdateDTOBuilder builder = LoanUpdateDTO.builder()
                .productName(loan.getProductName())
                .productType(loan.getProductType())
                .currency(loan.getCurrency())
                .security(loan.getSecurity())
                .loanAmount(loan.getPrincipalAmount())
                .tenureMonths(loan.getLoanTermMonths())
                .interestRatePercent(loan.getInterestRate())
                .repaymentFrequency(loan.getRepaymentFrequency())
                .interestType(loan.getInterestType())
                .processingFeeTZS(loan.getProcessingFee())
                .latePaymentFeeTZS(loan.getLatePaymentFee())
                .prepaymentPenaltyPercent(loan.getPrepaymentPenalty());

        loanFinanceRepository.findByLoan_LoanNumber(loan.getLoanNumber()).ifPresent(finance -> {
            BigDecimal outstanding = finance.getOutstandingAmount() != null ? finance.getOutstandingAmount() : BigDecimal.ZERO;
            BigDecimal paid = finance.getPaidAmount() != null ? finance.getPaidAmount() : BigDecimal.ZERO;
            
            builder.accruedInterest(finance.getAccruedInterest())
                    .outstandingAmount(outstanding)
                    .paidAmount(paid)
                    .installmentAmount(finance.getInstallmentAmount())
                    .totalAmountToBePaid(outstanding.add(paid));
        });

        return builder.build();
    }
}
