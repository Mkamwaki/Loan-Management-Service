package com.example.loantraking.controller;

import com.example.loantraking.dto.CollateralsDTO;
import com.example.loantraking.facade.CollateralsFacade;
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
public class CollateralsController {

    private final CollateralsFacade collateralsFacade;

    public CollateralsController(CollateralsFacade collateralsFacade) {
        this.collateralsFacade = collateralsFacade;
    }

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

    @GetMapping
    public ResponseEntity<List<CollateralsDTO>> getAll() {
        return ResponseEntity.ok(collateralsFacade.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CollateralsDTO> getById(@PathVariable Long id) {
        CollateralsDTO dto = collateralsFacade.getById(id);
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<CollateralsDTO>> getByCustomerId(@PathVariable String customerId) {
        return ResponseEntity.ok(collateralsFacade.getByCustomerId(customerId));
    }

    @GetMapping("/loan/{loanNumber}")
    public ResponseEntity<List<CollateralsDTO>> getByLoanNumber(@PathVariable String loanNumber) {
        return ResponseEntity.ok(collateralsFacade.getByLoanNumber(loanNumber));
    }
}
