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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "collaterals")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Collaterals {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Optional relation to a loan (LoanInfo)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_number", referencedColumnName = "loan_number")
    private LoanInfo loan;

    // Optional relation to the customer who owns the collateral
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "type")
    private String type; // e.g., VEHICLE, PROPERTY, CASH

    @Column(name = "registration_number")
    private String registrationNumber;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "estimated_value", precision = 19, scale = 4)
    private BigDecimal estimatedValue;

    @OneToMany(mappedBy = "collateral", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CollateralAttachment> attachments = new ArrayList<>();

    // Audit fields directly in entity
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
