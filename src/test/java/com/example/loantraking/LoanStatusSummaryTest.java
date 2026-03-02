package com.example.loantraking;

import com.example.loantraking.Entity.Customer;
import com.example.loantraking.Entity.LoanInfo;
import com.example.loantraking.dto.LoanSummaryDTO;
import com.example.loantraking.enums.CustomerStatus;
import com.example.loantraking.enums.loan_status;
import com.example.loantraking.repository.CustomerRepository;
import com.example.loantraking.repository.LoanInfoRepository;
import com.example.loantraking.service.LoanInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class LoanStatusSummaryTest {

    @Autowired
    private LoanInfoService loanInfoService;

    @Autowired
    private LoanInfoRepository loanInfoRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    @Transactional
    public void testGetLoanSummaryIncludesLoanStatus() {
        // Create a customer
        Customer customer = Customer.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .status(CustomerStatus.CUSTOMER)
                .build();
        customer = customerRepository.save(customer);

        // Create a loan with a specific status
        String loanNo = "L-STATUS-TEST";
        LoanInfo loan = LoanInfo.builder()
                .loanNumber(loanNo)
                .customer(customer)
                .status(loan_status.APPROVED)
                .build();
        loan = loanInfoRepository.save(loan);

        // Get summary
        LoanSummaryDTO summary = loanInfoService.getLoanSummaryByLoanNumber(loanNo);

        // Verify status
        assertThat(summary.getLoanStatus()).isEqualTo("APPROVED");
        System.out.println("[DEBUG_LOG] Loan status in summary: " + summary.getLoanStatus());
    }
}
