package com.example.loantraking.service;

import com.example.loantraking.Entity.Collaterals;

import java.util.List;
import java.util.Optional;

public interface CollateralsService {
    Collaterals create(Collaterals collaterals);
    Collaterals update(Long id, Collaterals collaterals);
    void delete(Long id);
    Optional<Collaterals> getById(Long id);
    List<Collaterals> getAll();
    List<Collaterals> getByCustomerId(String customerId);
    List<Collaterals> getByLoanNumber(String loanNumber);
}
