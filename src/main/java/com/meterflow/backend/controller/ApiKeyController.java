package com.meterflow.backend.controller;

import com.meterflow.backend.dto.response.ApiKeyResponse;
import com.meterflow.backend.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/keys")
public class ApiKeyController {

    @Autowired
    private ApiService apiService;

    @GetMapping
    public ResponseEntity<List<ApiKeyResponse>> getUserKeys(
            Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(apiService.getUserApiKeys(email));
    }

    @PatchMapping("/{id}/revoke")
    public ResponseEntity<String> revokeKey(
            @PathVariable UUID id,
            Authentication authentication) {
        String email = authentication.getName();
        apiService.revokeApiKey(id, email);
        return ResponseEntity.ok("Key revoked successfully");
    }
}