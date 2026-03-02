package com.example.loantraking.service;

import com.example.loantraking.Entity.Customer;
import com.example.loantraking.enums.CustomerStatus;
import com.example.loantraking.Entity.Relative;
import com.example.loantraking.repository.RelativeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RelativeServiceImpl implements RelativeService {

    private final RelativeRepository relativeRepository;
    private final CustomerService customerService;

    @Override
    @Transactional
    public Relative saveRelative(Relative relative) {
        log.info("Saving relative: {}", relative);
        Relative savedRelative = relativeRepository.save(relative);
        
        Customer customer = relative.getCustomer();
        if (customer != null && customer.getStatus() == CustomerStatus.PROSPECT) {
            log.info("Changing customer {} status from PROSPECT to CUSTOMER_NO_COLLATERAL", customer.getCustomerId());
            customer.setStatus(CustomerStatus.CUSTOMER);
            customerService.saveCustomer(customer);
        }
        
        return savedRelative;
    }

    @Override
    public Relative updateRelative(Relative relative) {
        log.info("Updating relative: {}", relative);
        if (!relativeRepository.existsById(relative.getRelativeId())) {
            throw new RuntimeException("Relative not found with id: " + relative.getRelativeId());
        }
        return relativeRepository.save(relative);
    }

    @Override
    public void deleteRelative(String id) {
        log.info("Deleting relative with id: {}", id);
        relativeRepository.deleteById(id);
    }

    @Override
    public Relative getRelativeById(String id) {
        log.info("Fetching relative by id: {}", id);
        return relativeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Relative not found with id: " + id));
    }

    @Override
    public List<Relative> getRelativesByCustomerId(String customerId) {
        log.info("Fetching relatives for customer id: {}", customerId);
        return relativeRepository.findByCustomerCustomerId(customerId);
    }
}
