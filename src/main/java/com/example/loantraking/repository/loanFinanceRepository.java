package com.example.loantraking.repository;

import com.example.loantraking.Entity.loanFinance;
import com.example.loantraking.enums.loan_status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface loanFinanceRepository extends JpaRepository<loanFinance, Long> {
    Optional<loanFinance> findByLoan_LoanNumber(String loanNumber);

    @Query("select coalesce(sum(f.accruedInterest), 0) from loanFinance f left join f.loan k where k.status = :status")
    BigDecimal sumAccruedInterestByStatus(loan_status status);

    @Query("select coalesce(sum(f.disbursedAmount), 0) from loanFinance f left join f.loan k where k.status = :status")
    BigDecimal sumDisbursedAmountByStatus(loan_status status);
}
