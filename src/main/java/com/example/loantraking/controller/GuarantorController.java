package com.example.loantraking.controller;

import com.example.loantraking.Entity.Guarantor;
import com.example.loantraking.dto.GuarantorDTO;
import com.example.loantraking.service.GuarantorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loan/guarantors")
@RequiredArgsConstructor
public class GuarantorController {

    private final GuarantorService guarantorService;

    @PostMapping("/add/{loanNumber}")
    public ResponseEntity<List<Guarantor>> addGuarantors(
            @PathVariable String loanNumber,
            @RequestBody List<GuarantorDTO> guarantorDTOs) {
        List<Guarantor> guarantors = guarantorService.addGuarantors(loanNumber, guarantorDTOs);
        return ResponseEntity.ok(guarantors);
    }

    @GetMapping("/loan/{loanNumber}")
    public ResponseEntity<List<Guarantor>> getGuarantorsByLoanNumber(@PathVariable String loanNumber) {
        List<Guarantor> guarantors = guarantorService.getGuarantorsByLoanNumber(loanNumber);
        return ResponseEntity.ok(guarantors);
    }
}
