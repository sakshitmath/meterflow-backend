package com.meterflow.backend.service;

import com.meterflow.backend.dto.response.UsageSummaryResponse;
import com.meterflow.backend.enums.KeyStatus;
import com.meterflow.backend.model.ApiKey;
import com.meterflow.backend.model.UsageLog;
import com.meterflow.backend.repository.ApiKeyRepository;
import com.meterflow.backend.repository.UsageLogRepository;
import com.meterflow.backend.repository.UserRepository;
import com.meterflow.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UsageService {

    @Autowired
    private UsageLogRepository usageLogRepository;

    @Autowired
    private ApiKeyRepository apiKeyRepository;

    @Autowired
    private UserRepository userRepository;

    public UsageSummaryResponse getUsageSummary(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<ApiKey> userKeys = apiKeyRepository.findByUserId(user.getId());

        List<UUID> keyIds = userKeys.stream()
                .map(ApiKey::getId)
                .collect(Collectors.toList());

        long totalRequests = keyIds.isEmpty() ? 0 :
                usageLogRepository.countByApiKeyIdIn(keyIds);

        long activeKeys = userKeys.stream()
                .filter(k -> k.getStatus() == KeyStatus.ACTIVE)
                .count();

        return new UsageSummaryResponse(totalRequests, activeKeys, totalRequests);
    }

    public List<UsageLog> getUsageLogs(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<ApiKey> userKeys = apiKeyRepository.findByUserId(user.getId());

        List<UUID> keyIds = userKeys.stream()
                .map(ApiKey::getId)
                .collect(Collectors.toList());

        if (keyIds.isEmpty()) return List.of();

        return usageLogRepository.findByApiKeyIdInOrderByTimestampDesc(keyIds);
    }
}