package com.meterflow.backend.controller;

import com.meterflow.backend.dto.response.BillingResponse;
import com.meterflow.backend.service.BillingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/billing")
public class BillingController {

    @Autowired
    private BillingService billingService;

    @GetMapping("/current")
    public ResponseEntity<BillingResponse> getCurrentBill(
            Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(billingService.getCurrentBill(email));
    }

    @GetMapping("/history")
    public ResponseEntity<List<BillingResponse>> getHistory(
            Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(billingService.getBillingHistory(email));
    }

    @PostMapping("/calculate")
    public ResponseEntity<BillingResponse> calculate(
            Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(billingService.calculateAndSaveBill(email));
    }
}