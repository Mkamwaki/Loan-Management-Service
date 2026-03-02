package com.example.loantraking.repository;

import com.example.loantraking.Entity.CustomerAttachment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerAttachmentRepository extends JpaRepository<CustomerAttachment, Long> {
    List<CustomerAttachment> findByCustomerCustomerId(String customerId);
    Page<CustomerAttachment> findByCustomerCustomerId(String customerId, Pageable pageable);
}
