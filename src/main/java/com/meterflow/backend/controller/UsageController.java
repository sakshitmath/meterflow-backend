package com.meterflow.backend.controller;

import com.meterflow.backend.dto.response.UsageSummaryResponse;
import com.meterflow.backend.model.UsageLog;
import com.meterflow.backend.service.UsageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/usage")
public class UsageController {

    @Autowired
    private UsageService usageService;

    @GetMapping("/summary")
    public ResponseEntity<UsageSummaryResponse> getSummary(
            Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(usageService.getUsageSummary(email));
    }

    @GetMapping("/logs")
    public ResponseEntity<List<UsageLog>> getLogs(
            Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(usageService.getUsageLogs(email));
    }
}