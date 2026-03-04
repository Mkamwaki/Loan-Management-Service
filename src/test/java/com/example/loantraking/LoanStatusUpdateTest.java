package com.example.loantraking;

import com.example.loantraking.Entity.Customer;
import com.example.loantraking.Entity.LoanInfo;
import com.example.loantraking.Entity.PaymentSchedule;
import com.example.loantraking.dto.LoanStatusUpdateDTO;
import com.example.loantraking.enums.CustomerStatus;
import com.example.loantraking.enums.PaymentScheduleStatus;
import com.example.loantraking.enums.loan_status;
import com.example.loantraking.repository.CustomerRepository;
import com.example.loantraking.repository.LoanInfoRepository;
import com.example.loantraking.repository.PaymentScheduleRepository;
import com.example.loantraking.service.LoanInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class LoanStatusUpdateTest {

    @Autowired
    private LoanInfoService loanInfoService;

    @Autowired
    private LoanInfoRepository loanInfoRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PaymentScheduleRepository paymentScheduleRepository;

    @Test
    @Transactional
    public void testUpdateLoanStatus() {
        // Create a customer
        Customer customer = Customer.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .status(CustomerStatus.CUSTOMER)
                .build();
        customer = customerRepository.save(customer);

        // Create a loan
        String loanNo = "L-UPDATE-TEST";
        LoanInfo loan = LoanInfo.builder()
                .loanNumber(loanNo)
                .customer(customer)
                .status(loan_status.PENDING)
                .build();
        loan = loanInfoRepository.save(loan);

        // Update status
        LoanStatusUpdateDTO updateDto = LoanStatusUpdateDTO.builder()
                .loanNumber(loanNo)
                .status(loan_status.APPROVED)
                .build();

        setJwtAuth();
        loanInfoService.updateLoanStatus(updateDto);
        SecurityContextHolder.clearContext();

        // Verify
        LoanInfo updatedLoan = loanInfoRepository.findByLoanNumber(loanNo).orElseThrow();
        assertThat(updatedLoan.getStatus()).isEqualTo(loan_status.APPROVED);
        System.out.println("[DEBUG_LOG] Loan status updated to: " + updatedLoan.getStatus());
    }

    @Test
    @Transactional
    public void testUpdateLoanStatusToDisbursedCreatesPaymentSchedule() {
        Customer customer = Customer.builder()
                .firstName("Diana")
                .lastName("Prince")
                .email("diana.prince@example.com")
                .status(CustomerStatus.CUSTOMER)
                .build();
        customer = customerRepository.save(customer);

        String loanNo = "L-DISBURSE-TEST";
        LoanInfo loan = LoanInfo.builder()
                .loanNumber(loanNo)
                .customer(customer)
                .status(loan_status.APPROVED)
                .principalAmount(new BigDecimal("1000.00"))
                .loanTermMonths(2)
                .repaymentFrequency("MONTHLY")
                .build();
        loanInfoRepository.save(loan);

        LoanStatusUpdateDTO disburseDto = LoanStatusUpdateDTO.builder()
                .loanNumber(loanNo)
                .status(loan_status.DISBURSED)
                .comment("Disbursed for testing")
                .build();

        setJwtAuth();
        loanInfoService.updateLoanStatus(disburseDto);
        SecurityContextHolder.clearContext();

        List<PaymentSchedule> scheduleRows = paymentScheduleRepository.findByLoan_LoanNumberOrderByPaymentNoAsc(loanNo);
        assertThat(scheduleRows).hasSize(2);
        assertThat(scheduleRows).allMatch(s -> s.getStatus() == PaymentScheduleStatus.NOT_PAID);
        assertThat(scheduleRows.get(0).getPaymentNo()).isEqualTo(1);
        assertThat(scheduleRows.get(1).getPaymentNo()).isEqualTo(2);
        assertThat(scheduleRows.get(0).getExpectedAmount().add(scheduleRows.get(1).getExpectedAmount()))
                .isEqualByComparingTo("1000.0000");
    }

    private void setJwtAuth() {
        Jwt jwt = Jwt.withTokenValue("test-token")
                .header("alg", "none")
                .claim("email", "tester@example.com")
                .build();
        SecurityContextHolder.getContext().setAuthentication(
                new JwtAuthenticationToken(jwt, AuthorityUtils.createAuthorityList("ROLE_USER"), "tester@example.com")
        );
    }
}
