package com.example.loantraking.Entity;

import com.example.loantraking.enums.interestType;
import com.example.loantraking.enums.productSecurity;
import com.example.loantraking.enums.currency;
import com.example.loantraking.enums.loanType;
import com.example.loantraking.enums.loan_status;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "LoanInfo")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loan_id")
    private Long loanId;

    @Column(name = "loan_number", unique = true, nullable = false)
    private String loanNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(name = "loan_type")
    private loanType loanType;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    private currency currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private loan_status status;

    @Column(name = "principal_amount", precision = 19, scale = 4)
    private BigDecimal principalAmount;

    @Column(name = "interest_rate", precision = 10, scale = 6)
    private BigDecimal interestRate;

    @Column(name = "loan_term_months")
    private Integer loanTermMonths;

    @Column(name = "repayment_frequency")
    private String repaymentFrequency;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_type")
    private String productType;

    @Enumerated(EnumType.STRING)
    @Column(name = "interest_type")
    private interestType interestType;

    @Enumerated(EnumType.STRING)
    @Column(name = "security")
    private productSecurity security;

    @Column(name = "processing_fee", precision = 19, scale = 4)
    private BigDecimal processingFee;

    @Column(name = "late_payment_fee", precision = 19, scale = 4)
    private BigDecimal latePaymentFee;

    @Column(name = "prepayment_penalty", precision = 19, scale = 4)
    private BigDecimal prepaymentPenalty;

    @Column(name = "purpose")
    private String purpose;

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
