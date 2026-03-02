package com.example.loantraking.repository;

import com.example.loantraking.Entity.LoanInfo;
import com.example.loantraking.enums.loan_status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoanInfoRepository extends JpaRepository<LoanInfo, Long> {
    Optional<LoanInfo> findByLoanNumber(String loanNumber);

    Page<LoanInfo> findByStatus(loan_status status, Pageable pageable);

    long countByStatus(loan_status status);
}
