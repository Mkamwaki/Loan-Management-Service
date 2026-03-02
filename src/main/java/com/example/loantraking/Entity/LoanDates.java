package com.example.loantraking.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "loan_dates")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanDates {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Reference the loan (LoanInfo) the dates belong to
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_number", referencedColumnName = "loan_number", nullable = false)
    private LoanInfo loan;

    @Column(name = "application_date")
    private LocalDate applicationDate;

    @Column(name = "approval_date")
    private LocalDate approvalDate;

    @Column(name = "disbursal_date")
    private LocalDate disbursalDate;

    @Column(name = "maturity_date")
    private LocalDate maturityDate;

    @Column(name = "first_repayment_date")
    private LocalDate firstRepaymentDate;

    @Column(name = "next_repayment_date")
    private LocalDate nextRepaymentDate;

    @Column(name = "last_repayment_date")
    private LocalDate lastRepaymentDate;

    // Audit fields directly
    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedBy
    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;
}
