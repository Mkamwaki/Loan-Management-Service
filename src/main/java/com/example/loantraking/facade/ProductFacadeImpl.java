package com.example.loantraking.facade;

import com.example.loantraking.Entity.Product;
import com.example.loantraking.dto.ProductDTO;
import com.example.loantraking.service.ProductService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductFacadeImpl implements ProductFacade {

    private final ProductService productService;

    public ProductFacadeImpl(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public ProductDTO create(ProductDTO dto) {
        Product p = dtoToEntity(dto);
        Product saved = productService.create(p);
        return entityToDto(saved);
    }

    @Override
    public ProductDTO update(Long id, ProductDTO dto) {
        Product p = dtoToEntity(dto);
        Product updated = productService.update(id, p);
        return entityToDto(updated);
    }

    @Override
    public void delete(Long id) {
        productService.delete(id);
    }

    @Override
    public ProductDTO getById(Long id) {
        return productService.getById(id).map(this::entityToDto).orElse(null);
    }

    @Override
    public List<ProductDTO> getAll() {
        return productService.getAll().stream().map(this::entityToDto).collect(Collectors.toList());
    }

    private Product dtoToEntity(ProductDTO dto) {
        if (dto == null) return null;
        Product p = new Product();
        p.setProductId(dto.getProductId());
        p.setProductCode(dto.getProductCode());
        p.setProductName(dto.getProductName());
        p.setProductType(dto.getProductType());
        p.setDescription(dto.getDescription());
        p.setCurrency(dto.getCurrency());
        p.setMinimumAmount(dto.getMinimumAmount());
        p.setMaximumAmount(dto.getMaximumAmount());
        p.setDefaultInterestRate(dto.getDefaultInterestRate());
        p.setInterestType(dto.getInterestType() == null ? null : dto.getInterestType());
        p.setMinimumTermMonths(dto.getMinimumTermMonths());
        p.setMaximumTermMonths(dto.getMaximumTermMonths());
        p.setRepaymentFrequency(dto.getRepaymentFrequency());
        p.setSecurity(dto.getSecurity());
        p.setProcessingFee(dto.getProcessingFee());
        p.setLatePaymentFee(dto.getLatePaymentFee());
        p.setPrepaymentPenalty(dto.getPrepaymentPenalty());
        p.setStatus(dto.getStatus() == null ? null : dto.getStatus());
        p.setEffectiveFrom(dto.getEffectiveFrom());
        p.setEffectiveTo(dto.getEffectiveTo());
        p.setMicrofinanceId(dto.getMicrofinanceId());
        return p;
    }

    private ProductDTO entityToDto(Product p) {
        if (p == null) return null;
        return ProductDTO.builder()
                .productId(p.getProductId())
                .productCode(p.getProductCode())
                .productName(p.getProductName())
                .productType(p.getProductType())
                .description(p.getDescription())
                .currency(p.getCurrency())
                .minimumAmount(p.getMinimumAmount())
                .maximumAmount(p.getMaximumAmount())
                .defaultInterestRate(p.getDefaultInterestRate())
                .interestType(p.getInterestType())
                .minimumTermMonths(p.getMinimumTermMonths())
                .maximumTermMonths(p.getMaximumTermMonths())
                .repaymentFrequency(p.getRepaymentFrequency())
                .security(p.getSecurity())
                .processingFee(p.getProcessingFee())
                .latePaymentFee(p.getLatePaymentFee())
                .prepaymentPenalty(p.getPrepaymentPenalty())
                .status(p.getStatus())
                .effectiveFrom(p.getEffectiveFrom())
                .effectiveTo(p.getEffectiveTo())
                .microfinanceId(p.getMicrofinanceId())
                .createdBy(p.getCreatedBy())
                .createdAt(p.getCreatedAt())
                .updatedBy(p.getUpdatedBy())
                .updatedAt(p.getUpdatedAt())
                .build();
    }
}
