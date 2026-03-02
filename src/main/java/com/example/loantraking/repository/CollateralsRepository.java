package com.example.loantraking.repository;

import com.example.loantraking.Entity.Collaterals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollateralsRepository extends JpaRepository<Collaterals, Long> {
    List<Collaterals> findByCustomerCustomerId(String customerId);
    List<Collaterals> findByLoanLoanNumber(String loanNumber);
}
