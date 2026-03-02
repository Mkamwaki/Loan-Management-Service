package com.example.loantraking.service;

import com.example.loantraking.Entity.Product;
import com.example.loantraking.exception.DuplicateResourceException;
import com.example.loantraking.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product create(Product product) {
        if (product.getProductCode() != null && productRepository.existsByProductCode(product.getProductCode())) {
            throw new DuplicateResourceException("Product with code " + product.getProductCode() + " already exists");
        }
        return productRepository.save(product);
    }

    @Override
    public Product update(Long id, Product product) {
        Product existing = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Product not found"));
        // copy mutable fields
        existing.setProductCode(product.getProductCode());
        existing.setProductName(product.getProductName());
        existing.setProductType(product.getProductType());
        existing.setDescription(product.getDescription());
        existing.setCurrency(product.getCurrency());
        existing.setMinimumAmount(product.getMinimumAmount());
        existing.setMaximumAmount(product.getMaximumAmount());
        existing.setDefaultInterestRate(product.getDefaultInterestRate());
        existing.setInterestType(product.getInterestType());
        existing.setMinimumTermMonths(product.getMinimumTermMonths());
        existing.setMaximumTermMonths(product.getMaximumTermMonths());
        existing.setRepaymentFrequency(product.getRepaymentFrequency());
        existing.setSecurity(product.getSecurity());
        existing.setProcessingFee(product.getProcessingFee());
        existing.setLatePaymentFee(product.getLatePaymentFee());
        existing.setPrepaymentPenalty(product.getPrepaymentPenalty());
        existing.setStatus(product.getStatus());
        existing.setEffectiveFrom(product.getEffectiveFrom());
        existing.setEffectiveTo(product.getEffectiveTo());
        existing.setMicrofinanceId(product.getMicrofinanceId());
        return productRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> getById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> getByCode(String code) {
        return productRepository.findByProductCode(code);
    }
}

