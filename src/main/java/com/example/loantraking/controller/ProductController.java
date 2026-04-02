package com.example.loantraking.controller;

import com.example.loantraking.dto.ProductDTO;
import com.example.loantraking.exception.DuplicateResourceException;
import com.example.loantraking.facade.ProductFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("loan/products")
@Tag(name = "Product Management", description = "APIs for managing loan products (loan types, interest rates, etc.)")
public class ProductController {

    private final ProductFacade productFacade;

    public ProductController(ProductFacade productFacade) {
        this.productFacade = productFacade;
    }

    @Operation(summary = "Create a new loan product")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody ProductDTO dto) {
        try {
            productFacade.create(dto);
            Map<String, String> res = new HashMap<>();
            res.put("message", "Product created successfully");
            return new ResponseEntity<>(res, HttpStatus.CREATED);
        } catch (DuplicateResourceException ex) {
            Map<String, String> err = new HashMap<>();
            err.put("error", ex.getMessage());
            return new ResponseEntity<>(err, HttpStatus.CONFLICT);
        } catch (Exception ex) {
            Map<String, String> err = new HashMap<>();
            err.put("error", ex.getMessage());
            return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Update an existing loan product")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ProductDTO dto) {
        try {
            productFacade.update(id, dto);
            Map<String, String> res = new HashMap<>();
            res.put("message", "Product updated successfully");
            return ResponseEntity.ok(res);
        } catch (IllegalArgumentException ex) {
            Map<String, String> err = new HashMap<>();
            err.put("error", ex.getMessage());
            return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            Map<String, String> err = new HashMap<>();
            err.put("error", ex.getMessage());
            return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Delete a loan product by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            productFacade.delete(id);
            Map<String, String> res = new HashMap<>();
            res.put("message", "Product deleted successfully");
            return ResponseEntity.ok(res);
        } catch (Exception ex) {
            Map<String, String> err = new HashMap<>();
            err.put("error", ex.getMessage());
            return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get all loan products")
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAll() {
        return ResponseEntity.ok(productFacade.getAll());
    }

    @Operation(summary = "Get a loan product by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getById(@PathVariable Long id) {
        ProductDTO dto = productFacade.getById(id);
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Get a loan product by product code")
    @GetMapping("/code/{code}")
    public ResponseEntity<ProductDTO> getByCode(@PathVariable String code) {
        // try find by code via facade
        List<ProductDTO> all = productFacade.getAll();
        return all.stream().filter(p -> code.equals(p.getProductCode())).findFirst()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
