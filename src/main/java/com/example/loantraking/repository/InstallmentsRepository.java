package com.example.loantraking.repository;

import com.example.loantraking.Entity.Installments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstallmentsRepository extends JpaRepository<Installments, Long> {
    List<Installments> findByLoan_LoanNumber(String loanNumber);
}

