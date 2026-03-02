package com.example.loantraking.service;

import com.example.loantraking.Entity.Collaterals;
import com.example.loantraking.repository.CollateralsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CollateralsServiceImpl implements CollateralsService {

    private final CollateralsRepository collateralsRepository;

    public CollateralsServiceImpl(CollateralsRepository collateralsRepository) {
        this.collateralsRepository = collateralsRepository;
    }

    @Override
    public Collaterals create(Collaterals collaterals) {
        return collateralsRepository.save(collaterals);
    }

    @Override
    public Collaterals update(Long id, Collaterals collaterals) {
        Collaterals existing = collateralsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Collateral not found"));
        if (collaterals.getLoan() != null) {
            existing.setLoan(collaterals.getLoan());
        }
        existing.setCustomer(collaterals.getCustomer());
        existing.setType(collaterals.getType());
        existing.setRegistrationNumber(collaterals.getRegistrationNumber());
        existing.setDescription(collaterals.getDescription());
        existing.setEstimatedValue(collaterals.getEstimatedValue());
        return collateralsRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        collateralsRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Collaterals> getById(Long id) {
        return collateralsRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Collaterals> getAll() {
        return collateralsRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Collaterals> getByCustomerId(String customerId) {
        return collateralsRepository.findByCustomerCustomerId(customerId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Collaterals> getByLoanNumber(String loanNumber) {
        return collateralsRepository.findByLoanLoanNumber(loanNumber);
    }
}
