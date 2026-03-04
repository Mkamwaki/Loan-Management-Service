package com.example.loantraking.repository;

import com.example.loantraking.Entity.PaymentSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentScheduleRepository extends JpaRepository<PaymentSchedule, Long> {
    List<PaymentSchedule> findByLoan_LoanNumberOrderByPaymentNoAsc(String loanNumber);

    boolean existsByLoan_LoanNumber(String loanNumber);

    void deleteByLoan_LoanNumber(String loanNumber);
}
