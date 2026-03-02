package com.example.loantraking.service;

import com.example.loantraking.Entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Product create(Product product);
    Product update(Long id, Product product);
    void delete(Long id);
    Optional<Product> getById(Long id);
    List<Product> getAll();
    Optional<Product> getByCode(String code);
}

