package com.example.loantraking;

import com.example.loantraking.Entity.Customer;
import com.example.loantraking.Entity.LoanInfo;
import com.example.loantraking.dto.LoanStatusUpdateDTO;
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
public class LoanStatusUpdateTest {

    @Autowired
    private LoanInfoService loanInfoService;

    @Autowired
    private LoanInfoRepository loanInfoRepository;

    @Autowired
    private CustomerRepository customerRepository;

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

        loanInfoService.updateLoanStatus(updateDto);

        // Verify
        LoanInfo updatedLoan = loanInfoRepository.findByLoanNumber(loanNo).orElseThrow();
        assertThat(updatedLoan.getStatus()).isEqualTo(loan_status.APPROVED);
        System.out.println("[DEBUG_LOG] Loan status updated to: " + updatedLoan.getStatus());
    }
}
