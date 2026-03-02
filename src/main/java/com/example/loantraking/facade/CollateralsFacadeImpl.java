package com.example.loantraking.facade;

import com.example.loantraking.Entity.CollateralAttachment;
import com.example.loantraking.Entity.Collaterals;
import com.example.loantraking.Entity.Customer;
import com.example.loantraking.Entity.LoanInfo;
import com.example.loantraking.dto.CollateralAttachmentDTO;
import com.example.loantraking.dto.CollateralsDTO;
import com.example.loantraking.repository.CustomerRepository;
import com.example.loantraking.repository.LoanInfoRepository;
import com.example.loantraking.service.CollateralsService;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class CollateralsFacadeImpl implements CollateralsFacade {

    private final CollateralsService collateralsService;
    private final CustomerRepository customerRepository;
    private final LoanInfoRepository loanInfoRepository;
    private static final String UPLOAD_DIR = "uploads/attachments/";

    public CollateralsFacadeImpl(CollateralsService collateralsService,
                                 CustomerRepository customerRepository,
                                 LoanInfoRepository loanInfoRepository) {
        this.collateralsService = collateralsService;
        this.customerRepository = customerRepository;
        this.loanInfoRepository = loanInfoRepository;
    }

    @Override
    public CollateralsDTO create(CollateralsDTO dto, List<MultipartFile> files) {
        Collaterals entity = dtoToEntity(dto);
        
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                String path = saveFile(file);
                CollateralAttachment attachment = CollateralAttachment.builder()
                        .name(file.getOriginalFilename())
                        .path(path)
                        .collateral(entity)
                        .build();
                entity.getAttachments().add(attachment);
            }
        }
        
        Collaterals saved = collateralsService.create(entity);
        return entityToDto(saved);
    }

    private String saveFile(MultipartFile file) {
        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename().trim().replaceAll("[\\\\/:*?\"<>|]", "_");
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);

            return filePath.toString();
        } catch (IOException e) {
            throw new RuntimeException("Could not store file. Error: " + e.getMessage());
        }
    }

    @Override
    public CollateralsDTO update(Long id, CollateralsDTO dto) {
        Collaterals entity = dtoToEntity(dto);
        Collaterals updated = collateralsService.update(id, entity);
        return entityToDto(updated);
    }

    @Override
    public void delete(Long id) {
        collateralsService.delete(id);
    }

    @Override
    public CollateralsDTO getById(Long id) {
        return collateralsService.getById(id).map(this::entityToDto).orElse(null);
    }

    @Override
    public List<CollateralsDTO> getAll() {
        return collateralsService.getAll().stream().map(this::entityToDto).collect(Collectors.toList());
    }

    @Override
    public List<CollateralsDTO> getByCustomerId(String customerId) {
        return collateralsService.getByCustomerId(customerId).stream().map(this::entityToDto).collect(Collectors.toList());
    }

    @Override
    public List<CollateralsDTO> getByLoanNumber(String loanNumber) {
        return collateralsService.getByLoanNumber(loanNumber).stream().map(this::entityToDto).collect(Collectors.toList());
    }

    private Collaterals dtoToEntity(CollateralsDTO dto) {
        if (dto == null) return null;
        Collaterals c = new Collaterals();
        c.setId(dto.getId());
        if (dto.getCustomerId() != null) {
            Customer cust = customerRepository.findById(dto.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + dto.getCustomerId()));
            c.setCustomer(cust);
        } else {
            c.setCustomer(null);
        }
        if (dto.getLoanNumber() != null) {
            LoanInfo loan = loanInfoRepository.findByLoanNumber(dto.getLoanNumber())
                    .orElseThrow(() -> new RuntimeException("Loan not found with number: " + dto.getLoanNumber()));
            c.setLoan(loan);
            if (c.getCustomer() == null && loan.getCustomer() != null) {
                c.setCustomer(loan.getCustomer());
            }
        } else {
            c.setLoan(null);
        }
        c.setType(dto.getType());
        c.setRegistrationNumber(dto.getRegistrationNumber());
        c.setDescription(dto.getDescription());
        c.setEstimatedValue(dto.getEstimatedValue());
        return c;
    }

    private CollateralsDTO entityToDto(Collaterals c) {
        if (c == null) return null;
        
        String loanNumber = null;
        if (c.getLoan() != null) {
            try {
                loanNumber = c.getLoan().getLoanNumber();
            } catch (Exception e) {
                // If lazy loading fails, we might be able to get it from the repository if we have the ID
                // But for now, let's just log or ignore
            }
        }

        List<CollateralAttachmentDTO> attachmentDTOs = new ArrayList<>();
        if (c.getAttachments() != null) {
            attachmentDTOs = c.getAttachments().stream()
                    .map(a -> CollateralAttachmentDTO.builder()
                            .id(a.getId())
                            .name(a.getName())
                            .path(a.getPath())
                            .collateralId(c.getId())
                            .createdBy(a.getCreatedBy())
                            .createdDate(a.getCreatedDate())
                            .lastModifiedBy(a.getLastModifiedBy())
                            .lastModifiedDate(a.getLastModifiedDate())
                            .build())
                    .collect(Collectors.toList());
        }

        return CollateralsDTO.builder()
                .id(c.getId())
                .customerId(c.getCustomer() != null ? c.getCustomer().getCustomerId() : null)
                .loanNumber(loanNumber)
                .type(c.getType())
                .registrationNumber(c.getRegistrationNumber())
                .description(c.getDescription())
                .estimatedValue(c.getEstimatedValue())
                .attachments(attachmentDTOs)
                .createdBy(c.getCreatedBy())
                .createdDate(c.getCreatedDate())
                .lastModifiedBy(c.getLastModifiedBy())
                .lastModifiedDate(c.getLastModifiedDate())
                .build();
    }
}
