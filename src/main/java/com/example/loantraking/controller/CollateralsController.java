package com.example.loantraking.controller;

import com.example.loantraking.dto.CollateralsDTO;
import com.example.loantraking.facade.CollateralsFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("loan/collaterals")
@Tag(name = "Collateral Management", description = "APIs for managing loan collaterals")
public class CollateralsController {

    private final CollateralsFacade collateralsFacade;

    public CollateralsController(CollateralsFacade collateralsFacade) {
        this.collateralsFacade = collateralsFacade;
    }

    @Operation(summary = "Create a new collateral with optional files")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> create(@RequestPart("collateral") CollateralsDTO dto,
                                   @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        try {
            CollateralsDTO created = collateralsFacade.create(dto, files);
            Map<String, Object> res = new HashMap<>();
            res.put("message", "Collateral created successfully");
            return new ResponseEntity<>(res, HttpStatus.CREATED);
        } catch (Exception ex) {
            Map<String, String> err = new HashMap<>();
            err.put("error", ex.getMessage());
            return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Update collateral information")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody CollateralsDTO dto) {
        try {
            CollateralsDTO updated = collateralsFacade.update(id, dto);
            Map<String, Object> res = new HashMap<>();
            res.put("message", "Collateral updated successfully");
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

    @Operation(summary = "Delete collateral by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            collateralsFacade.delete(id);
            Map<String, String> res = new HashMap<>();
            res.put("message", "Collateral deleted successfully");
            return ResponseEntity.ok(res);
        } catch (Exception ex) {
            Map<String, String> err = new HashMap<>();
            err.put("error", ex.getMessage());
            return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get all collaterals")
    @GetMapping
    public ResponseEntity<List<CollateralsDTO>> getAll() {
        return ResponseEntity.ok(collateralsFacade.getAll());
    }

    @Operation(summary = "Get collateral by ID")
    @GetMapping("/{id}")
    public ResponseEntity<CollateralsDTO> getById(@PathVariable Long id) {
        CollateralsDTO dto = collateralsFacade.getById(id);
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Get collaterals by customer ID")
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<CollateralsDTO>> getByCustomerId(@PathVariable String customerId) {
        return ResponseEntity.ok(collateralsFacade.getByCustomerId(customerId));
    }

    @Operation(summary = "Get collaterals by loan number")
    @GetMapping("/loan/{loanNumber}")
    public ResponseEntity<List<CollateralsDTO>> getByLoanNumber(@PathVariable String loanNumber) {
        return ResponseEntity.ok(collateralsFacade.getByLoanNumber(loanNumber));
    }
}
