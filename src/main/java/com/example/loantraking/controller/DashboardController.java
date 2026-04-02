package com.example.loantraking.controller;

import com.example.loantraking.dto.DashboardSummaryDTO;
import com.example.loantraking.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/loan/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "APIs for dashboard summaries and statistics")
public class DashboardController {

    private final DashboardService dashboardService;

    @Operation(summary = "Get dashboard summary statistics")
    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryDTO> getSummary() {
        return ResponseEntity.ok(dashboardService.getSummary());
    }
}
