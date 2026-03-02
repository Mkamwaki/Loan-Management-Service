package com.example.loantraking.repository;

import com.example.loantraking.Entity.Relative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelativeRepository extends JpaRepository<Relative, String> {
    List<Relative> findByCustomerCustomerId(String customerId);
}
