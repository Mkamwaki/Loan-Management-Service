package com.example.loantraking.Entity;

import com.example.loantraking.enums.loan_status;
import jakarta.persistence.*;

import java.time.LocalDateTime;

public class LoanStatus {
    @Id
    private Long id;
    // Reference the loan (LoanInfo) the dates belong to
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_number", referencedColumnName = "loan_number", nullable = false)
    private LoanInfo loan;
    @Column(name = "loan_status", nullable = false)
    private loan_status loan_status;
    @Column(name = "rejection_reason")
    private String Rejection_reason;
    //audit fields
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime created_at;
    @Column(name = "updated_at")
    private LocalDateTime updated_at;
    @Column(name = "created_by", nullable = false, updatable = false)
    private String created_by;
    @Column(name = "updated_by")
    private String updated_by;


}


