package com.example.loantraking.Entity;

import com.example.loantraking.enums.currency;
import com.example.loantraking.enums.interestType;
import com.example.loantraking.enums.productSecurity;
import com.example.loantraking.enums.productstatus;
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
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "products", uniqueConstraints = @UniqueConstraint(columnNames = "product_code"))
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_code", nullable = false, unique = true)
    private String productCode;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "product_type")
    private String productType; // e.g., LOAN, DEPOSIT, CARD

    @Column(name = "description", length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    private currency currency;

    // Financial configuration
    @Column(name = "minimum_amount", precision = 19, scale = 4)
    private BigDecimal minimumAmount;

    @Column(name = "maximum_amount", precision = 19, scale = 4)
    private BigDecimal maximumAmount;

    @Column(name = "default_interest_rate", precision = 10, scale = 6)
    private BigDecimal defaultInterestRate;

    @Column(name = "interest_type")
    private interestType interestType; // FIXED / FLOATING

    @Column(name = "minimum_term_months")
    private Integer minimumTermMonths;

    @Column(name = "maximum_term_months")
    private Integer maximumTermMonths;

    @Column(name = "repaymentFrequency")
    private String repaymentFrequency;

    @Enumerated(EnumType.STRING)
    @Column(name = "security")
    private productSecurity security; // SECURED / UNSECURED

    // Fees & Charges
    @Column(name = "processing_fee", precision = 19, scale = 4)
    private BigDecimal processingFee;

    @Column(name = "late_payment_fee", precision = 19, scale = 4)
    private BigDecimal latePaymentFee;

    @Column(name = "prepayment_penalty", precision = 19, scale = 4)
    private BigDecimal prepaymentPenalty;

    // Status
    @Column(name = "status")
    private productstatus status; // ACTIVE / INACTIVE

    @Column(name = "effective_from")
    private LocalDate effectiveFrom;

    @Column(name = "effective_to")
    private LocalDate effectiveTo;

    // Microfinance identifier
    @Column(name = "microfinance_id")
    private String microfinanceId;

    // Audit fields (directly on entity)
    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedBy
    @Column(name = "updated_by")
    private String updatedBy;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}

