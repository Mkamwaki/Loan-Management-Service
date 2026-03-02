package com.example.loantraking.facade;

import com.example.loantraking.dto.CollateralsDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CollateralsFacade {
    CollateralsDTO create(CollateralsDTO dto, List<MultipartFile> files);
    CollateralsDTO update(Long id, CollateralsDTO dto);
    void delete(Long id);
    CollateralsDTO getById(Long id);
    List<CollateralsDTO> getAll();
    List<CollateralsDTO> getByCustomerId(String customerId);
    List<CollateralsDTO> getByLoanNumber(String loanNumber);
}
