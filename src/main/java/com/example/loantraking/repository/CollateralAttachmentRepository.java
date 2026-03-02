package com.example.loantraking.repository;

import com.example.loantraking.Entity.CollateralAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollateralAttachmentRepository extends JpaRepository<CollateralAttachment, Long> {
    List<CollateralAttachment> findByCollateralId(Long collateralId);
}
