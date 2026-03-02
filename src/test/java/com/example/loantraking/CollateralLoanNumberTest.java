package com.example.loantraking;

import com.example.loantraking.Entity.Collaterals;
import com.example.loantraking.Entity.Customer;
import com.example.loantraking.Entity.LoanInfo;
import com.example.loantraking.dto.CollateralsDTO;
import com.example.loantraking.enums.CustomerStatus;
import com.example.loantraking.facade.CollateralsFacade;
import com.example.loantraking.repository.CollateralsRepository;
import com.example.loantraking.repository.CustomerRepository;
import com.example.loantraking.repository.LoanInfoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CollateralLoanNumberTest {

    @Autowired
    private CollateralsFacade collateralsFacade;

    @Autowired
    private LoanInfoRepository loanInfoRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CollateralsRepository collateralsRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Transactional
    public void testCreateCollateralWithLoanNumberActuallySavesInDb() {
        // Create a customer
        Customer customer = Customer.builder()
                .firstName("Test")
                .lastName("Customer")
                .email("test@example.com")
                .status(CustomerStatus.CUSTOMER)
                .build();
        customer = customerRepository.save(customer);

        // Create a loan
        String loanNo = "L-001";
        LoanInfo loan = LoanInfo.builder()
                .loanNumber(loanNo)
                .customer(customer)
                .build();
        loan = loanInfoRepository.save(loan);

        // Create a collateral DTO
        CollateralsDTO dto = CollateralsDTO.builder()
                .loanNumber(loanNo)
                .type("VEHICLE")
                .registrationNumber("ABC-123")
                .description("Test car")
                .estimatedValue(new BigDecimal("10000.0000"))
                .build();

        // Save collateral through facade
        CollateralsDTO savedDto = collateralsFacade.create(dto, Collections.emptyList());
        assertThat(savedDto.getId()).isNotNull();
        assertThat(savedDto.getLoanNumber()).isEqualTo(loanNo);

        // Check the database directly using JDBC to see if the column is NOT NULL
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                "SELECT loan_number FROM collaterals WHERE id = ?", savedDto.getId());
        
        assertThat(rows).hasSize(1);
        String savedLoanNumber = (String) rows.get(0).get("loan_number");
        
        System.out.println("[DEBUG_LOG] Saved loan_number in DB: " + savedLoanNumber);
        
        assertThat(savedLoanNumber).isEqualTo(loanNo);
    }

    @Test
    @Transactional
    public void testUpdateCollateralWithoutLoanNumberDoesNotSetItToNull() {
        // Create a customer
        Customer customer = Customer.builder()
                .firstName("Test")
                .lastName("Customer")
                .email("test2@example.com")
                .status(CustomerStatus.CUSTOMER)
                .build();
        customer = customerRepository.save(customer);

        // Create a loan
        String loanNo = "L-002";
        LoanInfo loan = LoanInfo.builder()
                .loanNumber(loanNo)
                .customer(customer)
                .build();
        loan = loanInfoRepository.save(loan);

        // Create a collateral with loan
        Collaterals collateral = Collaterals.builder()
                .loan(loan)
                .customer(customer)
                .type("PROPERTY")
                .description("Initial description")
                .build();
        collateral = collateralsRepository.save(collateral);
        Long id = collateral.getId();

        // Verify it has loan_number in DB
        String initialLoanNo = jdbcTemplate.queryForObject(
                "SELECT loan_number FROM collaterals WHERE id = ?", String.class, id);
        assertThat(initialLoanNo).isEqualTo(loanNo);

        // Update collateral but DON'T provide loanNumber in DTO
        CollateralsDTO updateDto = CollateralsDTO.builder()
                .description("Updated description")
                // loanNumber is null here
                .build();

        collateralsFacade.update(id, updateDto);

        // Check if loan_number is still there or if it became NULL
        String finalLoanNo = jdbcTemplate.queryForObject(
                "SELECT loan_number FROM collaterals WHERE id = ?", String.class, id);
        
        System.out.println("[DEBUG_LOG] After update, loan_number in DB: " + finalLoanNo);
        
        // This is where I suspect it might fail (becoming NULL)
        assertThat(finalLoanNo).isEqualTo(loanNo);
    }
}
