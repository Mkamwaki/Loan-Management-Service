package com.example.loantraking.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "loan_finance")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class loanFinance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_number", referencedColumnName = "loan_number")
    private LoanInfo loan;

    @Column(name = "disbursed_amount", precision = 19, scale = 4)
    private BigDecimal disbursedAmount;

    @Column(name = "outstanding_amount", precision = 19, scale = 4)
    private BigDecimal outstandingAmount;

    @Column(name = "fees", precision = 19, scale = 4)
    private BigDecimal fees;

    @Column(name = "accrued_interest", precision = 19, scale = 4)
    private BigDecimal accruedInterest;

    @Column(name = "paid_amount", precision = 19, scale = 4)
    @Builder.Default
    private BigDecimal paidAmount = BigDecimal.ZERO;

    @Column(name = "installment_amount", precision = 19, scale = 4)
    private BigDecimal installmentAmount;

    // Audit fields
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
