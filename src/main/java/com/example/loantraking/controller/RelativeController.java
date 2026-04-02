package com.example.loantraking.controller;

import com.example.loantraking.dto.RelativeDTO;
import com.example.loantraking.facade.RelativeFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("loan/relatives")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Relative Management", description = "APIs for managing customer relatives (next of kin, etc.)")
public class RelativeController {

    private final RelativeFacade relativeFacade;

    @Operation(summary = "Create a new relative for a customer")
    @PostMapping
    public ResponseEntity<RelativeDTO> createRelative(@Valid @RequestBody RelativeDTO relativeDTO) {
        log.info("Received request to create relative: {}", relativeDTO);
        return ResponseEntity.ok(relativeFacade.createRelative(relativeDTO));
    }

    @Operation(summary = "Update relative information")
    @PutMapping("/{id}")
    public ResponseEntity<RelativeDTO> updateRelative(@PathVariable String id, @Valid @RequestBody RelativeDTO relativeDTO) {
        log.info("Received request to update relative with id: {}", id);
        return ResponseEntity.ok(relativeFacade.updateRelative(id, relativeDTO));
    }

    @Operation(summary = "Delete a relative by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRelative(@PathVariable String id) {
        log.info("Received request to delete relative with id: {}", id);
        relativeFacade.deleteRelative(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get a relative by ID")
    @GetMapping("/{id}")
    public ResponseEntity<RelativeDTO> getRelative(@PathVariable String id) {
        log.info("Received request to get relative with id: {}", id);
        return ResponseEntity.ok(relativeFacade.getRelative(id));
    }

    @Operation(summary = "Get all relatives for a specific customer")
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<RelativeDTO>> getRelativesByCustomer(@PathVariable String customerId) {
        log.info("Received request to get relatives for customer id: {}", customerId);
        return ResponseEntity.ok(relativeFacade.getRelativesByCustomer(customerId));
    }
}
