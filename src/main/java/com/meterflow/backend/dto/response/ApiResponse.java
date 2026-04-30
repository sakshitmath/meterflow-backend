package com.meterflow.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ApiResponse {
    private UUID id;
    private String name;
    private String baseUrl;
    private String description;
    private Boolean isActive;
    private LocalDateTime createdAt;
}