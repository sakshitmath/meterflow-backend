package com.meterflow.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ApiKeyResponse {
    private UUID id;
    private String keyValue;
    private String status;
    private String apiName;
    private LocalDateTime createdAt;
}