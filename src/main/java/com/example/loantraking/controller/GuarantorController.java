package com.example.loantraking.controller;

import com.example.loantraking.Entity.Guarantor;
import com.example.loantraking.dto.GuarantorDTO;
import com.example.loantraking.service.GuarantorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loan/guarantors")
@RequiredArgsConstructor
@Tag(name = "Guarantor Management", description = "APIs for managing loan guarantors")
public class GuarantorController {

    private final GuarantorService guarantorService;

    @Operation(summary = "Add guarantors to a loan")
    @PostMapping("/add/{loanNumber}")
    public ResponseEntity<List<Guarantor>> addGuarantors(
            @PathVariable String loanNumber,
            @RequestBody List<GuarantorDTO> guarantorDTOs) {
        List<Guarantor> guarantors = guarantorService.addGuarantors(loanNumber, guarantorDTOs);
        return ResponseEntity.ok(guarantors);
    }

    @Operation(summary = "Get guarantors by loan number")
    @GetMapping("/loan/{loanNumber}")
    public ResponseEntity<List<Guarantor>> getGuarantorsByLoanNumber(@PathVariable String loanNumber) {
        List<Guarantor> guarantors = guarantorService.getGuarantorsByLoanNumber(loanNumber);
        return ResponseEntity.ok(guarantors);
    }
}
