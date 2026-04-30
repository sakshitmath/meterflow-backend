package com.meterflow.backend.controller;

import com.meterflow.backend.model.ApiKey;
import com.meterflow.backend.model.UsageLog;
import com.meterflow.backend.repository.ApiKeyRepository;
import com.meterflow.backend.repository.UsageLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/gateway")
public class GatewayController {

    @Autowired
    private ApiKeyRepository apiKeyRepository;

    @Autowired
    private UsageLogRepository usageLogRepository;

    @RequestMapping("/**")
    public ResponseEntity<String> gateway(
            @RequestHeader("X-API-KEY") String apiKeyValue,
            HttpServletRequest request) {

        // Step 1 - Validate API Key
        Optional<ApiKey> apiKeyOpt = apiKeyRepository
                .findByKeyValueAndStatus(apiKeyValue,
                        com.meterflow.backend.enums.KeyStatus.ACTIVE);

        if (apiKeyOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid API Key");
        }

        ApiKey apiKey = apiKeyOpt.get();


        // Step 2 - Log the request
        long startTime = System.currentTimeMillis();

        UsageLog log = UsageLog.builder()
                .apiKey(apiKey)
                .endpoint(request.getRequestURI())
                .method(request.getMethod())
                .timestamp(LocalDateTime.now())
                .build();

        // Step 3 - Forward to actual API
        String baseUrl = apiKey.getApi().getBaseUrl();
        String path = request.getRequestURI()
                .replace("/gateway", "");
        String targetUrl = baseUrl + path;

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate
                    .getForEntity(targetUrl, String.class);
            log.setResponseStatus(response.getStatusCode().value());
            log.setLatencyMs(System.currentTimeMillis() - startTime);
            usageLogRepository.save(log);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            log.setResponseStatus(500);
            log.setLatencyMs(System.currentTimeMillis() - startTime);
            usageLogRepository.save(log);
            return ResponseEntity.status(500)
                    .body("Gateway error: " + e.getMessage());
        }
    }
}