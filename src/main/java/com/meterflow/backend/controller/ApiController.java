package com.meterflow.backend.controller;

import com.meterflow.backend.dto.request.CreateApiRequest;
import com.meterflow.backend.dto.response.ApiKeyResponse;
import com.meterflow.backend.dto.response.ApiResponse;
import com.meterflow.backend.service.ApiService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/apis")
public class ApiController {

    @Autowired
    private ApiService apiService;

    @PostMapping
    public ResponseEntity<ApiResponse> createApi(
            @Valid @RequestBody CreateApiRequest request,
            Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(apiService.createApi(email, request));
    }

    @GetMapping
    public ResponseEntity<List<ApiResponse>> getUserApis(
            Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(apiService.getUserApis(email));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteApi(
            @PathVariable UUID id,
            Authentication authentication) {
        String email = authentication.getName();
        apiService.deleteApi(id, email);
        return ResponseEntity.ok("API deleted successfully");
    }

    @PostMapping("/{id}/keys")
    public ResponseEntity<ApiKeyResponse> generateKey(
            @PathVariable UUID id,
            Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(apiService.generateApiKey(id, email));
    }
}