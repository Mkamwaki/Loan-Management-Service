package com.example.loantraking.repository;

import com.example.loantraking.Entity.Guarantor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuarantorRepository extends JpaRepository<Guarantor, Long> {
    List<Guarantor> findByLoanLoanNumber(String loanNumber);
}
