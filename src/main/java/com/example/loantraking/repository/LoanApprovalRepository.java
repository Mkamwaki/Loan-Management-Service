package com.example.loantraking.repository;

import com.example.loantraking.Entity.LoanApproval;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanApprovalRepository extends JpaRepository<LoanApproval, Long> {
    List<LoanApproval> findByReferenceId(String referenceId);
}
