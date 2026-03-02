package com.example.loantraking.facade;

import com.example.loantraking.dto.ProductDTO;

import java.util.List;

public interface ProductFacade {
    ProductDTO create(ProductDTO dto);
    ProductDTO update(Long id, ProductDTO dto);
    void delete(Long id);
    ProductDTO getById(Long id);
    List<ProductDTO> getAll();
}

