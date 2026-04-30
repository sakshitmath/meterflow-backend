package com.meterflow.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsageSummaryResponse {
    private long totalRequests;
    private long activeKeys;
    private long thisMonthRequests;
}