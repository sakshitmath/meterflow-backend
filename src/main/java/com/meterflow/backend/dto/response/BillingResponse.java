package com.meterflow.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class BillingResponse {
    private UUID id;
    private Long totalRequests;
    private BigDecimal amountDue;
    private String status;
    private LocalDateTime billingPeriodStart;
    private LocalDateTime billingPeriodEnd;
    private LocalDateTime createdAt;
}